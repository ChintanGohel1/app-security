package com.services;

import java.util.HashMap;
import java.util.List;
import com.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entity.Role;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;
import com.google.common.base.Joiner;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleDAO;

	@Override
	public Role save(Role role) throws AlreadyExist {

		if (!isExistRole(role)) {

			return roleDAO.save(role);

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>();
			mapErrors.put("email", "Role already Exist.");
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));

		}

	}

	@Override
	public List<Role> findAll() {
		return roleDAO.findAll();
	}

	@Override
	public Role findOne(Long id) throws EntityNotFound {

		Role role = roleDAO.findOne(id);

		if (null != role) {
			return role;
		} else {

			throw new EntityNotFound("Role not found.");
		}

	}

	@Override
	public void delete(Long id) {

		if (id != null)
			roleDAO.delete(id);

	}

	@Override
	public void delete(Role role) {
		if (role != null)
			roleDAO.delete(role);

	}

	public boolean isExistRole(Role role) {

		Role savedRole = roleDAO.findByRoleName(role.getRoleName());

		if (null != savedRole && savedRole.getId() != role.getId()) {
			return true;
		}

		return false;

	}
}
