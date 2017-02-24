package com.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.request.CreateUserRequest;
import com.response.UserResponse;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Vinit Solanki
 *
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable, UserDetails {

	private static final long serialVersionUID = 6311364761937265306L;

	@NotEmpty(message = "{uservo.firstname.empty.message}")
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@NotEmpty(message = "{uservo.lastname.empty.message}")
	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	@NotEmpty
	@Email
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@NotEmpty
	@Column(name = "password", length = 250, nullable = false)
	private String password;

	//	@Transient
	//	private String confirmedPassword;

	@Column(name = "enabled")
	private boolean enabled;

	@OneToOne(fetch = FetchType.EAGER)
	//@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Role role;

	public User() {

	}

	public User(Long id) {
		super.id = id;
	}

	public User(UserResponse userDto) {
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.id = userDto.getId();
		this.email = userDto.getEmail();
		//		this.password = userVo.getPassword();
		this.enabled = userDto.isEnabled();
		if (null != userDto.getRole())
			this.role = new Role(userDto.getRole());
	}

	public User(CreateUserRequest userDto) {
		this.firstName = userDto.getFirstName();
		this.lastName = userDto.getLastName();
		this.id = userDto.getId();
		this.email = userDto.getEmail();
		this.password = userDto.getPassword();
		this.enabled = userDto.isEnabled();
		//		this.role = userVo.getRole();
		if (null != userDto.getRole())
			this.role = new Role(userDto.getRole());
	}

	public String getUsername() {
		return email;
	}

	public void setUsername(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Transient
	@JsonIgnore
	public Set<Permission> getPermissions() {
		Set<Permission> perms = new HashSet<Permission>();
		if (null != role)
			perms.addAll(role.getPermissions());
		return perms;
	}

	@Override
	@Transient
	@JsonIgnore
	public Set<GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (null != getRole())
			authorities.add(getRole());
		authorities.addAll(getPermissions());
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.getEnabled();
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

	@Override
	public String toString() {
		return String.format("%s(id=%d, username=%s, password=%s, enabled=%b)",
				this.getClass().getSimpleName(), this.getId(),
				this.getUsername(), this.getPassword(), this.getEnabled());
	}

	/*public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}*/

}
