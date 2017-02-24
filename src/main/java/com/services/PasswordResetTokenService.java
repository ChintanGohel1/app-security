package com.services;

import com.entity.PasswordResetToken;
import com.entity.User;

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
