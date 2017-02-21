package com.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Vinit Solanki
 *
 */
public class AuthUserDTO {

	@Email(message = "{uservo.email.invalid.message}")
	@NotEmpty(message = "{uservo.email.empty.message}")
	private String email;
	@NotEmpty(message = "{uservo.password.empty.message}")
	private String password;

	private boolean isRememberMe;

	public AuthUserDTO() {

	}

	public AuthUserDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return isRememberMe;
	}

	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}

}
