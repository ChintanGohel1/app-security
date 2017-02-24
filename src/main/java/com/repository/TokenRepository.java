package com.repository;

import org.springframework.data.repository.CrudRepository;

import com.entity.AuthToken;
import com.entity.User;

/**
 * @author Vinit Solanki
 *
 */
public interface TokenRepository extends CrudRepository<AuthToken, Long> {

	public AuthToken findFistByTokenAndRemoteAddress(final String token, final String remoteAddress);

	public AuthToken findFistByUserAndRemoteAddress(final User user, final String remoteAddress);
	

}
