package com.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.entities.Permission;
import com.services.PermissionService;

/**
 * @author Vinit Solanki
 *
 */
@RestController
@RequestMapping(value = "/permission")
// @PreAuthorize("hasAuthority('ADMIN')")
public class PermissionController {

	private static final Logger log = Logger.getLogger(PermissionController.class);
	
	@Autowired
	private PermissionService permissionService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_LIST_GET')")
	public ResponseEntity<?> findAll() {

		List<Permission> permissions = permissionService.findAll();

		return ResponseEntity.ok(permissions);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('USER_POST')")
	public ResponseEntity<?> save(@Valid @RequestBody Permission permission) {
		
			Permission savedPermission = permissionService.save(permission);

			return ResponseEntity.ok(savedPermission);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_GET')")
	public ResponseEntity<?> findOne(@PathVariable("id") Long id) {

		return ResponseEntity.ok(permissionService.findOne(id));

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('USER_PUT')")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
			@Valid @RequestBody Permission permission) {

		if (id != permission.getId())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"permission id mismatch");

		Permission updatedPermission = permissionService.save(permission);

		return ResponseEntity.ok(updatedPermission);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('USER_DELETE')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {

		permissionService.findOne(id);

		permissionService.delete(id);

		return ResponseEntity.ok().build();

	}

}
