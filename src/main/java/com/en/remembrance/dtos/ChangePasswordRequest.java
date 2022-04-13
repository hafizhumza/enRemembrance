
package com.en.remembrance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

	@NotNull(message = "Old password cannot be null")
	private String currentPassword;

	@NotNull(message = "New password cannot be null")
	private String newPassword;

	@NotNull(message = "Confirm password cannot be null")
	private String confirmPassword;

}
