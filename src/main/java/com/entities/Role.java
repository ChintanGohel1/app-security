package com.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import com.dto.RoleDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Vinit Solanki
 *
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 6874667425302308430L;

	@NotEmpty(message = "{role.rolename.empty.message}")
	@Size(max = 50)
	@Column(name = "rolename", length = 50)
	private String roleName;

	//	@OneToMany(fetch = FetchType.EAGER)
	//	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") })
	//	private Set<User> userRoles;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "role_permissions", joinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id", referencedColumnName = "id") })
	private Set<Permission> permissions = new HashSet<Permission>();

	public Role() {
		super();
	}

	public Role(Long id) {
		super();
		this.id = id;
	}

	public Role(RoleDTO roleDto) {
		super();
		this.id = roleDto.getId();
		this.roleName = roleDto.getRoleName();
		this.permissions = roleDto.getPermissions();
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	//	@JsonIgnore
	//	public Set<User> getUserRoles() {
	//		return userRoles;
	//	}
	//
	//	public void setUserRoles(Set<User> userRoles) {
	//		this.userRoles = userRoles;
	//	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return String.format("%s(id=%d, rolename='%s')", this.getClass()
		        .getSimpleName(), this.getId(), this.getRoleName());
	}

	@Override
	@JsonIgnore
	public String getAuthority() {
		return getRoleName();
	}

}
