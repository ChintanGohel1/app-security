package com.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.entities.AuthToken;
import com.entities.User;

/**
 * @author Vinit Solanki
 *
 */
public interface TokenDAO extends CrudRepository<AuthToken, Long> {

	
	public AuthToken findFistByTokenAndRemoteAddress(final String token, final String remoteAddress);

	public AuthToken findFistByUserAndRemoteAddress(final User user, final String remoteAddress);
	

}
