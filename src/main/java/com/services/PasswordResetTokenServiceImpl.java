package com.services;

import java.util.Date;

import com.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entity.PasswordResetToken;
import com.entity.User;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenDao;

	@Override
	public PasswordResetToken save(PasswordResetToken passwordResetToken) {

		PasswordResetToken existedPasswordResetToken = findOneByUser(passwordResetToken.getUser());

		if (null != existedPasswordResetToken) {
			passwordResetToken.setId(existedPasswordResetToken.getId());
		}

		passwordResetToken.setCreatedOn(new Date());

		passwordResetTokenDao.save(passwordResetToken);

		return existedPasswordResetToken;

	}

	@Override
	public PasswordResetToken findPasswordResetToken(User user, String token) {

		return passwordResetTokenDao.findOneByUserAndToken(user, token);
	}

	@Override
	public void delete(PasswordResetToken passwordResetToken) {

		passwordResetTokenDao.delete(passwordResetToken);

	}

	@Override
	public PasswordResetToken findOneByUser(User user) {

		return passwordResetTokenDao.findOneByUser(user);
	}

}
