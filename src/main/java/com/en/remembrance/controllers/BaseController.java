package com.en.remembrance.controllers;

import com.en.remembrance.constants.Constant;
import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.security.dto.AuthUserDetail;
import com.en.remembrance.security.utilities.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


@Slf4j
public class BaseController {

    protected String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);

        if (exception instanceof BadCredentialsException)
            return "Invalid email and password!";
        else if (exception instanceof LockedException)
            return exception.getMessage();

        return "Invalid email and password!";
    }

    protected String getCurrentUserRole() {
        Collection<? extends GrantedAuthority> authorities = AuthUtil.getCurrentUserAuthorities();

        if (!authorities.iterator().hasNext()) {
            log.error("User Role not found");
            throw new InvalidAccessException("User Role not found");
        }

        return authorities.iterator().next().getAuthority();
    }

    protected UserDto setUserModel(Model model) {
        UserDto currentUser = getCurrentUserDto();
        model.addAttribute(Constant.KEY_USER, currentUser);
        return currentUser;
    }

    protected UserDto getCurrentUserDto() {
        AuthUserDetail user = AuthUtil.getCurrentUser();
        // TODO: Hardcoded String "ROLE_"
        return new UserDto(user.getUserId(), user.getFullName(), user.getUsername(), user.getDob(), getCurrentUserRole().replace("ROLE_", ""));
    }

    protected void setMessages(Model model, String resultMessage, String errorMessage) {
        model.addAttribute(Constant.KEY_ERROR, errorMessage);
        model.addAttribute(Constant.KEY_RESULT_MESSAGE, resultMessage);
    }

}
