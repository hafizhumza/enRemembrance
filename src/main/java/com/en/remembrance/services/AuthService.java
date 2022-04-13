package com.en.remembrance.services;

import org.springframework.stereotype.Service;


@Service
public class AuthService extends BaseService {

//    public CurrentUserDto login(LoginModel model) {
//
//        //        LoginResponse response = authServerClient.login(AuthUtil.getClientBasicAuthHeader(), RequestUtil.getLoginRequest(model));
//        //        Result<UserResponse> result = authServerClient.userinfo(AuthUtil.getBearerToken(response.getAccess_token()));
//        //        logIfError(result);
//        //
//        //        UserSession session = new UserSession();
//        //        session.setData(response, result.getData());
//        //        return session;
//
//        CurrentUserDto session = new CurrentUserDto();
//        session.setId(1L);
//        session.setEmail("hamzamughal11@hotmail.com");
//        session.setAccountNonLocked(true);
//        session.setName("Hafiz Hamza");
//        session.setRole("User");
//        session.setScope("User");
//        return session;
//    }

    /*public void register(RegisterUserRequest request) {
        Optional<AuthUser> existingUser = userRepository.findByEmail(userDto.getEmail());

        if (existingUser.isPresent())
            duplicateEmail();

        Role role = roleService.findByName(ConstUtil.ROLE_USER);

        AuthUser authUser = new AuthUser();
        authUser.setFullName(userDto.getFullName().trim());
        authUser.setEmail(userDto.getEmail().trim());
        authUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(false);
        authUser.setCredentialsNonExpired(true);
        authUser.setEnabled(true);
        authUser.setRoles(Collections.singletonList(role));

        userRepository.save(authUser);
        Result<String> result = authServerClient.register(request);
        throwIfInternalServerError(result);
        return result;
    }*/

    /*public Result<String> updateProfile(String token, UpdateUserRequest request) {
        Result<String> result = authServerClient.updateProfile(AuthUtil.getBearerToken(token), request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> forgotPassword(ForgotPasswordRequest request) {
        Result<String> result = authServerClient.forgotPassword(request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<ForgotPasswordVerifiedResponse> verifyForgotPasswordToken(String token) {
        Result<ForgotPasswordVerifiedResponse> result = authServerClient.verifyForgotPasswordToken(token);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> changeForgotPassword(ChangeForgotPasswordRequest request) {
        Result<String> result = authServerClient.changeForgotPassword(request);
        throwIfInvalidAccess(result);
        return result;
    }

    public Result<String> changePassword(String token, ChangePasswordRequest request) {
        Result<String> result = authServerClient.changePassword(AuthUtil.getBearerToken(token), request);
        throwIfInvalidAccess(result);
        return result;
    }*/

}
