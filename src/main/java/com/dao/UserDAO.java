package com.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.entities.User;

/**
 * @author Vinit Solanki
 *
 */
public interface UserDAO extends PagingAndSortingRepository<User, Long> {

	default public User findByUsername(String username) {

		return findByEmail(username);

	}

	public Page<User> findByFirstName(Pageable pageable, String name);

	//public List<User> findByFirstName(String name);

	public List<User> findAll();

	public User findByEmail(String email);

}
