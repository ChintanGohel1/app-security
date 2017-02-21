package com.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.entities.Permission;

/**
 * @author Vinit Solanki
 *
 */
public interface PermissionDAO extends CrudRepository<Permission, Long> {

	public Permission findByPermissionName(String permissionName);

	public List<Permission> findAll();

}
