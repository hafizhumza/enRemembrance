package com.en.remembrance.dtos;

import com.en.remembrance.domain.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    private String name;

    private String email;

    private Long dob;

    private String role;

    public UserDto(AuthUser user) {
        this.id = user.getId();
        this.name = user.getFullName();
        this.email = user.getEmail();
        this.dob = user.getDob();
        // TODO: Hardcoded list index (0)
        // TODO: Hardcoded String "ROLE_"
        this.role = user.getRoles().get(0).getName().replace("ROLE_", "");
    }

}
