package com.test.entities;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Vinit Solanki
 *
 */
public class AuthenticationRequest {

	private static final long serialVersionUID = 6624726180748515507L;
	private String email;
	private String password;

	public AuthenticationRequest() {
		super();
	}

	public AuthenticationRequest(String email, String password) {
		this.setEmail(email);
		this.setPassword(password);
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
