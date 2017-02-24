package com.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.entity.Role;

/**
 * @author Vinit Solanki
 *
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByRoleName(String rolename);

	public List<Role> findAll();

}
