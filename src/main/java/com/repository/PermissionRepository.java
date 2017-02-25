package com.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.entity.Permission;

/**
 * @author Vinit Solanki
 *
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Permission findByPermissionName(String permissionName);

	Page<Permission> findAll(Pageable pageable);

}
