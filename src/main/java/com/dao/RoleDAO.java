package com.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.entities.Role;

/**
 * @author Vinit Solanki
 *
 */
public interface RoleDAO extends CrudRepository<Role, Long> {

	public Role findByRoleName(String rolename);

	public List<Role> findAll();

}
