package com.util;

import java.time.Duration;

import com.entity.AuthToken;
import com.entity.PasswordResetToken;

/**
 * @author Vinit Solanki
 *
 */
// @Component
public class TokenUtils {

	public static final String TOKEN_HEADER = "X-Auth-Token";
	//public static final Long TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 10; // 10 days
	public static final Long TOKEN_EXPIRATION_TIME = Duration.ofDays(10).toMillis();
	//public static final Long RESET_PASSWORD_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 2; // 2 Hours
	public static final Long RESET_PASSWORD_TOKEN_EXPIRATION_TIME = Duration.ofMinutes(30).toMillis(); // 2 Hours
	public static final int TOKEN_LENGTH = 35;

	public static String generateToken() {

		return StringUtils.generateRandomString(TOKEN_LENGTH);
	}

	public static String refreshToken() {
		return generateToken();
	}

	public static boolean isTokenExpired(AuthToken authToken) {
		return !authToken.isRememberMe() && ((System.currentTimeMillis() - authToken.getUpdatedOn().getTime()) > TOKEN_EXPIRATION_TIME);
	}

	public static boolean isTokenExpired(PasswordResetToken passwordResetToken) {
		return ((System.currentTimeMillis() - passwordResetToken.getCreatedOn().getTime()) > RESET_PASSWORD_TOKEN_EXPIRATION_TIME);
	}

	/**
	 * @param authToken
	 * @return true if AuthToken is valid else return false
	 */
	public static boolean isValidAuthToken(AuthToken authToken) {
		return (null != authToken && null != authToken.getUser());
		//return (null != authToken && null != authToken.getUser() && authToken.getUser().isEnabled());
	}

}