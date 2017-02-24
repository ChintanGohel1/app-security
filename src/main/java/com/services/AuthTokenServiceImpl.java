package com.services;

import java.util.Date;

import com.repository.TokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entity.AuthToken;
import com.entity.User;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class AuthTokenServiceImpl implements AuthTokenService {

	private static final Logger log = Logger.getLogger(AuthTokenServiceImpl.class);

	@Autowired
	private TokenRepository authTokenDAO;

	@Override
	public AuthToken save(final AuthToken authToken) {

		return authTokenDAO.save(authToken);
	}

	@Override
	public void delete(final AuthToken authToken) {

		authTokenDAO.delete(authToken);

	}

	@Override
	public AuthToken findFistByTokenAndRemoteAddress(String token, final String remoteAddress) {
		// TODO Auto-generated method stub
		return authTokenDAO.findFistByTokenAndRemoteAddress(token, remoteAddress);
	}

	@Override
	public AuthToken findFistByUserAndRemoteAddress(final User user, final String remoteAddress) {
		// TODO Auto-generated method stub
		return authTokenDAO.findFistByUserAndRemoteAddress(user, remoteAddress);
	}

	/**
	 * @param user
	 * @param token
	 * @param remoteAddress
	 */
	@Override
	public void processToGnerateAuthToken(User user, String token, String remoteAddress, String userAgent, boolean isRememberMe) {

		AuthToken existedAuthToken = findFistByUserAndRemoteAddress(user, remoteAddress);

		if (null != existedAuthToken) {
			log.info("Token already exist : " + user.getUsername());
			existedAuthToken.setToken(token);
			existedAuthToken.setUpdatedOn(new Date());
			existedAuthToken.setRememberMe(isRememberMe);
			save(existedAuthToken);

		} else {
			log.info("Token Created : " + user.getUsername());
			AuthToken authToken = new AuthToken();
			authToken.setCreatedOn(new Date());
			authToken.setRemoteAddress(remoteAddress);
			authToken.setUserAgent(userAgent);
			authToken.setUser(user);
			authToken.setToken(token);
			authToken.setUpdatedOn(new Date());
			authToken.setRememberMe(isRememberMe);
			save(authToken);
		}

	}

}
