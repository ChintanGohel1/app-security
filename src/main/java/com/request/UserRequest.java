package com.request;

import com.entity.Role;
import com.entity.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Vinit Solanki
 *
 */
public class UserRequest {

	private Long id;

	@NotEmpty(message = "{uservo.firstname.empty.message}")
	private String firstName;
	@NotEmpty(message = "{uservo.lastname.empty.message}")
	private String lastName;
	@Email(message = "{uservo.email.invalid.message}")
	@NotEmpty(message = "{uservo.email.empty.message}")
	private String email;
	//	@NotEmpty(message = "{uservo.password.empty.message}")
	//	private String password;

	//	private String confirmedPassword;

	private boolean enabled;

	private RoleRequest role;

	private boolean isRememberMe;

	public UserRequest() {
		super();
	}

	public UserRequest(Long id) {
		super();
		this.id = id;
	}

	public UserRequest(User user) {
		this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), new RoleRequest(user.getRole()));
	}

	public UserRequest(Long id, String firstName, String lastName, String email, RoleRequest role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		//		this.role = role;
		this.role = role;
	}

	public UserRequest(Long id, String firstName, String lastName, String email) {
		this(id, firstName, lastName, email, null);
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

	//	public String getPassword() {
	//		return this.password;
	//	}
	//
	//	public void setPassword(String password) {
	//		this.password = password;
	//	}

	//
	//	public String getConfirmedPassword() {
	//		return confirmedPassword;
	//	}
	//
	//	public void setConfirmedPassword(String confirmedPassword) {
	//		this.confirmedPassword = confirmedPassword;
	//	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public RoleRequest getRole() {
		return role;
	}

	public void setRole(RoleRequest role) {
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

	@Override
	public String toString() {
		return String.format("%s(id=%d, firstname=%s, lastname=%s, email=%s, role=%s)",
		        this.getClass().getSimpleName(), this.getId(),
		        this.getFirstName(), this.getLastName(), this.getEmail(), this.getRole());
	}
}