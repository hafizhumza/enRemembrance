package com.en.remembrance.controllers;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.domain.AuthUser;
import com.en.remembrance.dtos.ChangeForgotPasswordRequest;
import com.en.remembrance.dtos.ChangePasswordRequest;
import com.en.remembrance.dtos.ForgotPasswordRequest;
import com.en.remembrance.dtos.RegisterUserRequest;
import com.en.remembrance.dtos.UpdateUserRequest;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.exceptions.InvalidInputException;
import com.en.remembrance.exceptions.InvalidPasswordException;
import com.en.remembrance.exceptions.LinkExpiredException;
import com.en.remembrance.exceptions.RecordNotFoundException;
import com.en.remembrance.exceptions.RoleNotFoundException;
import com.en.remembrance.exceptions.UserAlreadyExistsException;
import com.en.remembrance.exceptions.UserNotFoundException;
import com.en.remembrance.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;


@Slf4j
@Transactional(readOnly = true)
@Controller
public class AuthController extends BaseController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/storytests")
    public String storytests() {
        return "storytests";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute(Constant.KEY_ERROR) String error, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String result, HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute(Constant.KEY_REGISTER_MODEL, new RegisterUserRequest());
        model.addAttribute(Constant.KEY_ERROR, error);
        session.setAttribute("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        return "login";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute(Constant.KEY_ERROR) String error, @ModelAttribute(Constant.KEY_RESULT_MESSAGE) String result, Model model) {
        model.addAttribute(Constant.KEY_REGISTER_MODEL, new RegisterUserRequest());
        model.addAttribute(Constant.KEY_ERROR, error);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, result);
        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String register(@Valid RegisterUserRequest registerUser, BindingResult bindingResult, Model model) {
        // TODO: Not verifying dob for now

        if (bindingResult.hasErrors()) {
            model.addAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors().get(0).getDefaultMessage());
            return "register";
        }

        boolean result = false;
        String message = "A verification email has been sent to your provided email address. Please verify your email address to continue";

        try {
            userService.register(registerUser);
            result = true;
        } catch (UserAlreadyExistsException e) {
            message = "User with this email is already registered";
        } catch (RoleNotFoundException e) {
            message = "Role not found";
        } catch (MessagingException e) {
            message = "Error while sending verification email";
        } catch (Exception e) {
            message = "An unknown error occurred on server side";
        }

        if (result) {
            model.addAttribute(Constant.KEY_RESULT_MESSAGE, message);
            model.addAttribute(Constant.KEY_REGISTER_MODEL, new RegisterUserRequest());
        } else {
            model.addAttribute(Constant.KEY_ERROR, message);
            model.addAttribute(Constant.KEY_REGISTER_MODEL, registerUser);
        }

        return "register";
    }

    @Transactional
    @GetMapping("/verify/{token}")
    public String verifyEmail(@PathVariable String token, Model model) {
        String message = "Email verified successfully!";
        boolean result = false;

        try {
            result = userService.verifyEmailVerificationToken(token);
        } catch (UserNotFoundException e) {
            message = "User not found against the token";
        } catch (InvalidAccessException e) {
            message = "Access denied";
        } catch (LinkExpiredException e) {
            message = "Link has been expired";
        } catch (Exception e) {
            message = "An unknown error occurred";
        }

        if (result) {
            model.addAttribute(Constant.KEY_RESULT_MESSAGE, message);
            return "login";
        }

        model.addAttribute(Constant.KEY_ERROR, message);
        return "invalid-link";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(@ModelAttribute(Constant.KEY_ERROR) String error, Model model) {
        model.addAttribute("forgotPassword", new ForgotPasswordRequest());
        model.addAttribute(Constant.KEY_ERROR, error);
        return "forgot";
    }

    @Transactional
    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid ForgotPasswordRequest request, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors());
            return "forgot";
        }

        String message = "Email verified successfully!";
        boolean result = false;

        try {
            userService.forgotPassword(request.getEmail());
            result = true;
        } catch (UserNotFoundException e) {
            message = "User not found against the token";
        } catch (MessagingException e) {
            message = "An error occurred while sending email";
        } catch (Exception e) {
            message = "An unknown error occurred";
        }

        if (result) {
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, message);
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, message);
        return "redirect:/forgot-password";
    }

    @Transactional
    @GetMapping("/forgot-password/{token}")
    public String verifyForgotPasswordToken(@PathVariable String token, Model model) {
        String message = "Email verified successfully!";
        boolean result = false;
        AuthUser user = null;

        try {
            user = userService.verifyForgotToken(token);
            result = true;
        } catch (UserNotFoundException e) {
            message = "User not found against the token";
        } catch (InvalidAccessException e) {
            message = "User not active";
        } catch (InvalidInputException e) {
            message = "Token not found";
        } catch (LinkExpiredException e) {
            message = "Link has been expired";
        } catch (Exception e) {
            message = "An unknown error occurred";
        }

        if (!result || user == null) {
            model.addAttribute(Constant.KEY_ERROR, message);
            return "forgot";
        }

        ChangeForgotPasswordRequest request = new ChangeForgotPasswordRequest();
        request.setId(user.getId());
        request.setToken(user.getForgotPassword().getToken());

        model.addAttribute(Constant.KEY_CHANGE_FORGOT_PASSWORD_MODEL, request);
        return "reset-password";
    }

    @Transactional
    @PostMapping("/change-forgot-password")
    public String changeForgotPassword(@Valid ChangeForgotPasswordRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:/forgot-password/" + request.getToken();
        }

        boolean result = false;

        try {
            result = userService.changeForgotPassword(request);
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "User not found");
        } catch (InvalidAccessException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, e.getMessage());
        } catch (RecordNotFoundException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Token not found");
        } catch (LinkExpiredException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Link is expired");
        } catch (InvalidInputException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Passwords do not match");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "An unknown error occurred");
        }

        if (!result) {
            return "redirect:/forgot-password/" + request.getToken();
        }

        redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Password changed successfully!");
        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePassword(@ModelAttribute(Constant.KEY_ERROR) String error, Model model) {
        model.addAttribute(Constant.KEY_CHANGE_PASSWORD_MODEL, new ChangePasswordRequest());
        model.addAttribute(Constant.KEY_ERROR, error);
        return "change-password";
    }

    @Transactional
    @PostMapping("/change-password")
    public String changePassword(@RequestHeader(value = "referer") final String referer, @Valid ChangePasswordRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        UserDto user = getCurrentUserDto();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:" + referer;
        }

        boolean result = false;

        try {
            result = userService.changePassword(user.getEmail(), request);
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "User not found");
        } catch (InvalidAccessException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "User account is locked");
        } catch (InvalidPasswordException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Current password is invalid");
        } catch (InvalidInputException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Passwords do not match");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "An unknown error occurred");
        }

        if (result) {
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Password changed successfully");
        }

        return "redirect:" + referer;
    }

    @Transactional
    @PostMapping("update-profile")
    public String updateProfile(@RequestHeader(value = "referer") final String referer, HttpServletRequest request, @Valid UpdateUserRequest updateUserRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        UserDto currentUser = getCurrentUserDto();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, bindingResult.getFieldErrors());
            return "redirect:" + referer;
        }

        boolean result = false;

        try {
            result = userService.updateUser(currentUser, updateUserRequest);
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "User not found");
        } catch (UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "Email already in use");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constant.KEY_ERROR, "An unknown error occurred");
        }

        if (result) {
            currentUser.setName(updateUserRequest.getName());
            currentUser.setEmail(updateUserRequest.getEmail());
            // TODO: Update user data in current session as well
            redirectAttributes.addFlashAttribute(Constant.KEY_RESULT_MESSAGE, "Profile updated successfully");
        }

        return "redirect:" + referer;
    }

    @RequestMapping("/success")
    public void loginPageRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        String role = getCurrentUserRole();

        if (role.contains("ROLE_ADMIN")) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/admin/home"));
        } else if (role.contains("ROLE_USER")) {
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/user/home"));
        } else {
            log.error("Invalid User Role: {}", role);
            throw new InvalidAccessException("Invalid User Role: " + role);
        }
    }

}
