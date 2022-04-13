
package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeForgotPasswordRequest {

	@Min(value = 1)
	private long id;

	@NotNull(message = "Password cannot be null")
	private String password;

	@NotNull(message = "Confirm password cannot be null")
	private String confirmPassword;

	@NotNull(message = "Token cannot be null")
	private String token;

}
