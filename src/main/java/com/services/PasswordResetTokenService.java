package com.services;

import com.entities.PasswordResetToken;
import com.entities.User;

/**
 * @author Vinit Solanki
 *
 */
public interface PasswordResetTokenService {

	PasswordResetToken save(PasswordResetToken passwordResetToken);

	PasswordResetToken findPasswordResetToken(User user, String token);

	void delete(PasswordResetToken passwordResetToken);

    PasswordResetToken findOneByUser(User user);

}
