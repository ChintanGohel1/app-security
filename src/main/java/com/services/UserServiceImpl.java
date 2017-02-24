package com.services;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import com.request.CreateUserRequest;
import com.request.UserRequest;
import com.response.UserResponse;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entity.User;
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
	private UserRepository userRepository;

	@Autowired
	private RoleService roleService;

	@Override
	public User loadUserByUsername(String username)
	        throws UsernameNotFoundException {

		return userRepository.findByUsername(username);

	}

	@Override
	public UserResponse findByEmail(String email) {

		User user = userRepository.findByEmail(email);

		if (null != user) {
			return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
		} else {
			throw new EntityNotFound("User not found.");
		}

	}

	@Override
	public UserResponse save(CreateUserRequest createUserRequest) throws AlreadyExist {

		System.out.println("Save UserService");
		System.out.println("createUserRequest.getId() = " + createUserRequest.getId());
		System.out.println("createUserRequest.getFirstName() = " + createUserRequest.getFirstName());
		System.out.println("createUserRequest.getRole() = " + createUserRequest.getRole());

		User user = new User(createUserRequest);

		System.out.println("Save UserService");
		System.out.println("user.getId() = " + user.getId());
		System.out.println("user.getFirstName() = " + user.getFirstName());
		System.out.println("user.getRole() = " + user.getRole());

		if (!isExistUser(user)) {

			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

			if (user.getId() == null)
				user.setId(0L);

			if (createUserRequest.getRole() != null)
				user.setRole(roleService.findOne(createUserRequest.getRole().getId()));

			return new UserResponse(userRepository.save(user));

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>();
			mapErrors.put("email", PropertiesUtil.setProprtyFile("ValidationMessages.properties").getProperty("uservo.email.exist.message"));
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));

		}

	}

	@Override
	public List<UserResponse> findAll() {

		List<User> users = userRepository.findAll();

		List<UserResponse> usersResponse = users.stream().map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole())).collect(Collectors.toList());

		return usersResponse;
	}

	@Override
	public UserResponse findOne(Long id) throws EntityNotFound {

		User user = userRepository.findOne(id);

		if (null != user) {
			return new UserResponse(user);
		} else {
			throw new EntityNotFound("User not found.");
		}

	}

	@Override
	public void delete(Long id) {

		if (id != null)
			userRepository.delete(id);

	}

	@Override
	public void delete(UserResponse userRequest) {

		if (userRequest != null)
			userRepository.delete(new User(userRequest));

	}

	public boolean isExistUser(User user) {

		User savedUser = userRepository.findByEmail(user.getEmail());

		if (null != savedUser && savedUser.getId() != user.getId()) {
			return true;
		}

		return false;

	}

	@Override
	public UserResponse update(UserRequest userRequest) throws AlreadyExist {

		//User user = new User(userRequest);
		User user = userRepository.findOne(userRequest.getId());

		if (!isExistUser(user)) {

			user.setEmail(userRequest.getEmail());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setEnabled(userRequest.isEnabled());
			user.setRole(roleService.findOne(userRequest.getRole().getId()));

			return new UserResponse(userRepository.save(user));

		} else {

			HashMap<String, String> mapErrors = new HashMap<String, String>();
			mapErrors.put("email", "email already exist.");
			throw new AlreadyExist(Joiner.on(" , ").withKeyValueSeparator(":").join(mapErrors));

		}

	}
}
