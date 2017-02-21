package com.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import com.entities.Role;
import com.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.validator.SamePasswords;

/**
 * @author Vinit Solanki
 *
 */
@SamePasswords(message = "{uservo.confirmedPassword.notmatch.message}")
public class CreateUserDTO {

	private Long id;

	@NotEmpty(message = "{uservo.firstname.empty.message}")
	private String firstName;
	@NotEmpty(message = "{uservo.lastname.empty.message}")
	private String lastName;
	@Email(message = "{uservo.email.invalid.message}")
	@NotEmpty(message = "{uservo.email.empty.message}")
	private String email;
	@NotEmpty(message = "{uservo.password.empty.message}")
	private String password;
	@NotEmpty(message = "{uservo.confirmedPassword.empty.message}")
	private String confirmedPassword;

	private boolean enabled;

	private RoleDTO role;

	private boolean isRememberMe;

	public CreateUserDTO() {

	}

	/**
	 * @param user
	 */
	public CreateUserDTO(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role.setId(user.getRole().getId());
		//this.enabled = user.isEnabled();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public RoleDTO getRole() {
		return role;
	}

	public void setRole(RoleDTO role) {
		this.role = role;
	}

	public boolean isRememberMe() {
		return isRememberMe;
	}

	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

}