package com.controller;

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

import com.entity.Role;
import com.services.RoleService;

/**
 * @author Vinit Solanki
 *
 */
@RestController
@RequestMapping(value = "/role")
// @PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

	private static final Logger log = Logger.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_LIST_GET')")
	public ResponseEntity<?> findAll() {

		List<Role> roles = roleService.findAll();

		return ResponseEntity.ok(roles);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('USER_POST')")
	public ResponseEntity<?> save(@Valid @RequestBody Role role) {

		Role savedRole = roleService.save(role);

		return ResponseEntity.ok(savedRole);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_GET')")
	public ResponseEntity<?> findOne(@PathVariable("id") Long id) {

		return ResponseEntity.ok(roleService.findOne(id));

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('USER_PUT')")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
	        @Valid @RequestBody Role role) {

		if (id != role.getId())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			        "role id mismatch");

		Role updatedRole = roleService.save(role);

		return ResponseEntity.ok(updatedRole);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('USER_DELETE')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {

		roleService.findOne(id);

		roleService.delete(id);

		return ResponseEntity.ok().build();

	}

}
