package com.en.remembrance.security.utilities;

import com.en.remembrance.security.dto.AuthUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;


public class AuthUtil {

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    public static AuthUserDetail getCurrentUser() {
        return (AuthUserDetail) getAuthentication().getPrincipal();
    }

    public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        return getAuthentication().getAuthorities();
    }

}
