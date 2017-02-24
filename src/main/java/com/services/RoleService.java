package com.services;

import java.util.List;


import com.entity.Role;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;

/**
 * @author Vinit Solanki
 *
 */
public interface RoleService {

	Role save(Role role) throws AlreadyExist;

	List<Role> findAll();

	Role findOne(Long id) throws EntityNotFound;

	void delete(Role role);

	void delete(Long id);

//	public void addRole(Role role);
//
//	public Role getRole(int roleId);
//
//	public Role getRole(String rolename);
//
//	public void updateRole(Role role);
//
//	public void deleteRole(int roleId);
//
//	public List<Role> getRoles();
//	
}
