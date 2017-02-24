package com.services;

import java.util.List;

import com.request.CreateUserRequest;
import com.request.UserRequest;
import com.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.entity.User;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;

/**
 * @author Vinit Solanki
 *
 */
public interface UserService extends UserDetailsService {

	UserResponse save(CreateUserRequest user) throws AlreadyExist;

	UserResponse update(UserRequest user) throws AlreadyExist;

	List<UserResponse> findAll();

	UserResponse findOne(Long id) throws EntityNotFound;

	void delete(UserResponse user);

	void delete(Long id);

	User loadUserByUsername(String userName);

	UserResponse findByEmail(String email);

}
