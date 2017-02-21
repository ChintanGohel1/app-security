package com.dto;

import java.util.HashSet;
import java.util.Set;

import com.entities.Permission;
import com.entities.Role;

/**
 * @author Vinit Solanki
 *
 */
public class RoleDTO {

	private long id;
	private String roleName;
	private Set<Permission> permissions = new HashSet<Permission>();

	public RoleDTO() {
		super();
	}

	public RoleDTO(long id) {
		super();
		this.id = id;
	}

	public RoleDTO(Role role) {
		super();
		this.id = role.getId();
		this.roleName = role.getRoleName();
		this.permissions = role.getPermissions();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

}
