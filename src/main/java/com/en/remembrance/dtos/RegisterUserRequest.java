package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotEmpty(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be empty")
    private String fullName;

    @Email(message = "Invalid email")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be empty")
    private String password;

}
