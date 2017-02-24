package com.services;

import java.util.List;


import com.entity.Permission;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;

/**
 * @author Vinit Solanki
 *
 */
public interface PermissionService {

	Permission save(Permission permission) throws AlreadyExist;

	List<Permission> findAll();

	Permission findOne(Long id) throws EntityNotFound;

	void delete(Permission permission);

	void delete(Long id);

//	public void addPermission(Permission permission);
//
//	public Permission getPermission(int permissionId);
//
//	public Permission getPermission(String permissionname);
//
//	public void updatePermission(Permission permission);
//
//	public void deletePermission(int permissionId);
//
//	public List<Permission> getPermissions();
//	
}
