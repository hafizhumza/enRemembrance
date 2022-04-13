package com.en.remembrance.security.dto;

import com.en.remembrance.domain.AuthUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class AuthUserDetail extends User {

    private Long userId;

    private String fullName;

    private Long dob;

    public AuthUserDetail(AuthUser authUser, Collection<? extends GrantedAuthority> authorities) {
        super(authUser.getEmail(), authUser.getPassword(), authUser.isEnabled(), authUser.isAccountNonExpired(), authUser.isCredentialsNonExpired(), authUser.isAccountNonLocked(), authorities);
        setUserId(authUser.getId());
        setFullName(authUser.getFullName());
        setDob(authUser.getDob());
    }

    public AuthUserDetail(long userId, String fullName, Long dob, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        setUserId(userId);
        setFullName(fullName);
        setDob(dob);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }
}
