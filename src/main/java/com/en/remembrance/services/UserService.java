package com.en.remembrance.services;

import com.en.remembrance.dtos.ChangeForgotPasswordRequest;
import com.en.remembrance.dtos.ChangePasswordRequest;
import com.en.remembrance.dtos.RegisterUserRequest;
import com.en.remembrance.dtos.UpdateUserRequest;
import com.en.remembrance.constants.Constant;
import com.en.remembrance.domain.AuthUser;
import com.en.remembrance.domain.ForgotPassword;
import com.en.remembrance.domain.Role;
import com.en.remembrance.dtos.EmailLinkDto;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.exceptions.InvalidInputException;
import com.en.remembrance.exceptions.InvalidPasswordException;
import com.en.remembrance.exceptions.LinkExpiredException;
import com.en.remembrance.exceptions.RecordNotFoundException;
import com.en.remembrance.exceptions.UserAlreadyExistsException;
import com.en.remembrance.exceptions.UserNotFoundException;
import com.en.remembrance.properties.ApplicationProperties;
import com.en.remembrance.repositories.UserRepository;
import com.en.remembrance.utilities.UrlBuilderUtil;
import com.en.remembrance.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService extends BaseService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final EmailService emailService;

    private final ForgotPasswordService forgotPasswordService;

    private final UrlBuilderUtil urlBuilderUtil;

    private final ApplicationProperties applicationProperties;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, EmailService emailService, ForgotPasswordService forgotPasswordService, UrlBuilderUtil urlBuilderUtil, ApplicationProperties applicationProperties, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.emailService = emailService;
        this.forgotPasswordService = forgotPasswordService;
        this.urlBuilderUtil = urlBuilderUtil;
        this.applicationProperties = applicationProperties;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser getByIdOrElseThrow(long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public List<UserDto> findAllActiveUsersExcludeSelfAndAdmin() {
        List<AuthUser> dbUsers = userRepository.findByEnabledTrueAndIdNotInAndRoles_Name(Collections.singletonList(getCurrentUserDto().getId()), Constant.ROLE_USER);

        if (CollectionUtils.isEmpty(dbUsers)) {
            new ArrayList<UserDto>();
        }

        return dbUsers.stream().map(UserDto::new).collect(Collectors.toList());
    }

    public void register(RegisterUserRequest request) throws MessagingException {
        Optional<AuthUser> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent())
            throw new UserAlreadyExistsException("User with this email already exists");

        Role role = roleService.findByNameOrElseThrow(Constant.ROLE_USER);

        AuthUser authUser = new AuthUser();
        authUser.setFullName(request.getFullName().trim());
        authUser.setEmail(request.getEmail().trim());
        authUser.setPassword(passwordEncoder.encode(request.getPassword()));
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setEnabled(false);
        authUser.setDob(request.getDob().getTime());
        authUser.setRoles(Collections.singletonList(role));

        String token = Util.getRandomUUIDWithSalt();
        String verificationUrl = urlBuilderUtil.createUrlWithToken("verify/", token);

        authUser.setVerificationToken(token);
        userRepository.save(authUser);

        EmailLinkDto emailLinkDto = new EmailLinkDto();
        emailLinkDto.setReceiverEmail(authUser.getEmail());
        emailLinkDto.setName(authUser.getFullName());
        emailLinkDto.setLink(verificationUrl);
        emailLinkDto.setExpiryDays(applicationProperties.security().getEmailVerificationExpiryDays());

        emailService.emailNewRegistration(emailLinkDto);
    }

    public void forgotPassword(String email) throws MessagingException {
        AuthUser user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String token = Util.getRandomUUIDWithSalt();

        ForgotPassword forgotPassword = user.getForgotPassword();

        if (forgotPassword == null) {
            forgotPassword = new ForgotPassword();
            forgotPassword.setUser(user);
        }

        forgotPassword.setAvailed(false);
        forgotPassword.setToken(token);
        forgotPassword.setCreateDate(System.currentTimeMillis());

        user.setForgotPassword(forgotPassword);
        userRepository.save(user);

        String link = urlBuilderUtil.createUrlWithToken("forgot-password/", token);

        EmailLinkDto emailLinkDto = new EmailLinkDto();
        emailLinkDto.setName(user.getFullName());
        emailLinkDto.setReceiverEmail(user.getEmail());
        emailLinkDto.setExpiryDays(applicationProperties.security().getForgotPasswordExpiryDays());
        emailLinkDto.setLink(link);

        emailService.emailForgotPassword(emailLinkDto);
    }

    public AuthUser verifyForgotToken(String token) {
        AuthUser authUser = userRepository.findByForgotPassword_Token(token).orElseThrow(UserNotFoundException::new);

        if (!authUser.isEnabled()) {
            throw new InvalidAccessException("User not active");
        }

        ForgotPassword forgotPassword = authUser.getForgotPassword();

        if (forgotPassword == null) {
            throw new InvalidInputException("Token not found");
        }

        if (Util.isExpired(applicationProperties.security().getForgotPasswordExpiryDays(), forgotPassword.getCreateDate())) {
            throw new LinkExpiredException("Link is expired");
        }

        return authUser;
    }

    public boolean changeForgotPassword(ChangeForgotPasswordRequest dto) {
        AuthUser user = userRepository.findByForgotPassword_Token(dto.getToken()).orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            invalidAccess("User is not active");
        }

        if (dto.getId() != user.getId()) {
            invalidAccess("Unauthorized user");
        }

        ForgotPassword forgotPassword = user.getForgotPassword();

        if (forgotPassword == null) {
            throw new RecordNotFoundException("Token not found");
        }

        if (Util.isExpired(applicationProperties.security().getForgotPasswordExpiryDays(), forgotPassword.getCreateDate())) {
            throw new LinkExpiredException("Link is expired");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            invalidInput("Passwords not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        forgotPassword.setAvailed(true);
        userRepository.save(user);
        return true;
    }

    public boolean changePassword(String email, ChangePasswordRequest dto) {
        AuthUser user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (!user.isEnabled()) {
            invalidAccess();
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            invalidInput();
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean updateUser(UserDto user, UpdateUserRequest request) {
        if (!request.getEmail().equals(user.getEmail())) {
            Optional<AuthUser> existingUser = userRepository.findByEmail(request.getEmail());

            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException();
            }
        }

        AuthUser dbUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        dbUser.setFullName(request.getName());

        if (request.getDob() != null) {
            dbUser.setDob(request.getDob().getTime());
        }

        dbUser.setEmail(request.getEmail());
        userRepository.save(dbUser);
        return true;
    }

    public boolean verifyEmailVerificationToken(String token) {
        AuthUser authUser = userRepository.findByForgotPassword_Token(token).orElseThrow(UserNotFoundException::new);

        if (authUser.isEnabled()) {
            throw new InvalidAccessException();
        }

        if (Util.isExpired(applicationProperties.security().getEmailVerificationExpiryDays(), authUser.getVerificationTokenMillis())) {
            throw new LinkExpiredException("Link is expired");
        }

        authUser.setEnabled(true);
        userRepository.save(authUser);
        return true;
    }

}
