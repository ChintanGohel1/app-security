package com.dao;

import org.springframework.data.repository.CrudRepository;

import com.entities.PasswordResetToken;
import com.entities.User;

/**
 * @author Vinit Solanki
 *
 */
public interface PasswordResetTokenDAO extends CrudRepository<PasswordResetToken, Long> {

	PasswordResetToken findOneByUserAndToken(User user, String token);

	PasswordResetToken findOneByUser(User user);

}
