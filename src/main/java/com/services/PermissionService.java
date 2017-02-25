package com.services;

import java.util.List;


import com.entity.Permission;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author Vinit Solanki
 *
 */
public interface PermissionService {

	Permission save(Permission permission) throws AlreadyExist;

	Page<Permission> findAll(Pageable pageable);

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
