package com.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.entity.Permission;

/**
 * @author Vinit Solanki
 *
 */
public interface PermissionRepository extends CrudRepository<Permission, Long> {

	public Permission findByPermissionName(String permissionName);

	public List<Permission> findAll();

}
