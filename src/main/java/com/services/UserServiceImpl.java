package com.services;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dao.RoleDAO;
import com.dao.UserDAO;
import com.dto.CreateUserDTO;
import com.dto.UserDTO;
import com.entities.User;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;
import com.google.common.base.Joiner;
import com.util.PropertiesUtil;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleService roleService;

	@Override
	public User loadUserByUsername(String username)
	        throws UsernameNotFoundException {

		return userDAO.findByUsername(username);

	}

	@Override
	public UserDTO findByEmail(String email) {

		User user = userDAO.findByEmail(email);

		if (null != user) {
			return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
		} else {
			throw new EntityNotFound("User not found.");
		}

	}

	@Override
	public UserDTO save(CreateUserDTO userVo) throws AlreadyExist {

		System.out.println("Save UserService");
		System.out.println("userVo.getId() = " + userVo.getId());
		System.out.println("userVo.getFirstName() = " + userVo.getFirstName());
		System.out.println("userVo.getRole() = " + userVo.getRole());

		User user = new User(userVo);

		System.out.println("Save UserService");
		System.out.println("user.getId() = " + user.getId());
		System.out.println("user.getFirstName() = " + user.getFirstName());
		System.out.println("user.getRole() = " + user.getRole());

		if (!isExistUser(user)) {

			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

			if (user.getId() == null)
				user.setId(0L);

			if (userVo.getRole() != null)
				user.setRole(roleService.findOne(userVo.getRole().getId()));

			return new UserDTO(userDAO.save(user));

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>();
			mapErrors.put("email", PropertiesUtil.setProprtyFile("ValidationMessages.properties").getProperty("uservo.email.exist.message"));
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));

		}

	}

	@Override
	public List<UserDTO> findAll() {

		List<User> users = userDAO.findAll();

		List<UserDTO> userVOs = users.stream().map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole())).collect(Collectors.toList());

		return userVOs;
	}

	@Override
	public UserDTO findOne(Long id) throws EntityNotFound {

		User user = userDAO.findOne(id);

		if (null != user) {
			return new UserDTO(user);
		} else {
			throw new EntityNotFound("User not found.");
		}

	}

	@Override
	public void delete(Long id) {

		if (id != null)
			userDAO.delete(id);

	}

	@Override
	public void delete(UserDTO userVo) {

		if (userVo != null)
			userDAO.delete(new User(userVo));

	}

	public boolean isExistUser(User user) {

		User savedUser = userDAO.findByEmail(user.getEmail());

		if (null != savedUser && savedUser.getId() != user.getId()) {
			return true;
		}

		return false;

	}

	/* (non-Javadoc)
	 * @see com.services.UserService#update(com.vo.UserVO)
	 */
	@Override
	public UserDTO update(UserDTO userVo) throws AlreadyExist {

		//User user = new User(userVo);
		User user = userDAO.findOne(userVo.getId());

		if (!isExistUser(user)) {

			user.setEmail(userVo.getEmail());
			user.setFirstName(userVo.getFirstName());
			user.setLastName(userVo.getLastName());
			user.setEnabled(userVo.isEnabled());
			user.setRole(roleService.findOne(userVo.getRole().getId()));

			return new UserDTO(userDAO.save(user));

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>();
			mapErrors.put("email", "email already exist.");
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));

		}

	}
}
