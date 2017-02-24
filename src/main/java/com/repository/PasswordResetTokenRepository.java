package com.repository;

import org.springframework.data.repository.CrudRepository;

import com.entity.PasswordResetToken;
import com.entity.User;

/**
 * @author Vinit Solanki
 *
 */
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

	PasswordResetToken findOneByUserAndToken(User user, String token);

	PasswordResetToken findOneByUser(User user);

}
