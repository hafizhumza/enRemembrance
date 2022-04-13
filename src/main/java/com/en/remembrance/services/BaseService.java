package com.en.remembrance.services;

import com.en.remembrance.dtos.UserDto;
import com.en.remembrance.exceptions.InvalidAccessException;
import com.en.remembrance.exceptions.InvalidInputException;
import com.en.remembrance.exceptions.RecordNotFoundException;
import com.en.remembrance.exceptions.ResponseException;
import com.en.remembrance.security.dto.AuthUserDetail;
import com.en.remembrance.security.utilities.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Slf4j
public class BaseService {

    public static void error() {
        throw new ResponseException();
    }

    public static void error(String message) {
        throw new ResponseException(message);
    }

    public static void notFound() {
        throw new RecordNotFoundException();
    }

    public static void invalidInput() {
        throw new InvalidInputException();
    }

    public static void invalidInput(String message) {
        throw new InvalidInputException(message);
    }

    public static void invalidAccess() {
        throw new InvalidAccessException();
    }

    public static void invalidAccess(String message) {
        throw new InvalidAccessException(message);
    }

    protected String getCurrentUserRole() {
        Collection<? extends GrantedAuthority> authorities = AuthUtil.getCurrentUserAuthorities();

        if (!authorities.iterator().hasNext()) {
            log.error("User Role not found");
            throw new InvalidAccessException("User Role not found");
        }

        return authorities.iterator().next().getAuthority();
    }

    protected UserDto getCurrentUserDto() {
        AuthUserDetail user = AuthUtil.getCurrentUser();
        // TODO: Hardcoded String "ROLE_"
        return new UserDto(user.getUserId(), user.getFullName(), user.getUsername(), user.getDob(), getCurrentUserRole().replace("ROLE_", ""));
    }

}
