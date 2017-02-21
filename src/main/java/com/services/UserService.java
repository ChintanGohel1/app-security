package com.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dto.CreateUserDTO;
import com.dto.UserDTO;
import com.entities.User;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;

/**
 * @author Vinit Solanki
 *
 */
public interface UserService extends UserDetailsService {

	UserDTO save(CreateUserDTO user) throws AlreadyExist;

	UserDTO update(UserDTO user) throws AlreadyExist;

	List<UserDTO> findAll();

	UserDTO findOne(Long id) throws EntityNotFound;

	void delete(UserDTO user);

	void delete(Long id);

	User loadUserByUsername(String userName);

	UserDTO findByEmail(String email);

}
