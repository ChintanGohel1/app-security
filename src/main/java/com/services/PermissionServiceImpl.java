package com.services;

import java.util.HashMap;

import com.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entity.Permission;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;
import com.google.common.base.Joiner;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionDAO;

	@Override
	public Permission save(Permission permission) throws AlreadyExist {

		if (!isExistPermission(permission)) {

			return permissionDAO.save(permission);

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>(){{put("email", "Permission already Exist.");}};
			//mapErrors.put("email", "Permission already Exist.");
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));
		}

	}

	@Override
	public Page<Permission> findAll(Pageable pageable) {
		return permissionDAO.findAll(pageable);
	}

	@Override
	public Permission findOne(Long id) throws EntityNotFound {

		Permission permission = permissionDAO.findOne(id);

		if (null != permission) {
			return permission;
		} else {
			throw new EntityNotFound("Permission not found.");
		}

	}

	@Override
	public void delete(Long id) {

		if (id != null)
			permissionDAO.delete(id);

	}

	@Override
	public void delete(Permission permission) {
		if (permission != null)
			permissionDAO.delete(permission);

	}

	public boolean isExistPermission(Permission permission) {

		Permission savedPermission = permissionDAO.findByPermissionName(permission.getPermissionName());

		if (null != savedPermission && savedPermission.getId() != permission.getId()) {
			return true;
		}

		return false;

	}
}
