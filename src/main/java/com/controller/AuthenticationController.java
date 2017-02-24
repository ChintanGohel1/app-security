package com.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.request.AuthUserRequest;
import com.response.TokenResponse;
import com.response.UserResponse;
import com.repository.TokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.entity.AuthToken;
import com.entity.User;
import com.services.AuthTokenService;
import com.util.TokenUtils;

/**
 * @author Vinit Solanki
 *
 */
@RestController
public class AuthenticationController {

	private static final Logger log = Logger.getLogger(AuthenticationController.class);

	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private AuthTokenService authTokenService;
	private TokenRepository tokenRepository;

	public AuthenticationController() {
		super();
	}

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, AuthTokenService authTokenService, TokenRepository tokenRepository) {
		super();
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.authTokenService = authTokenService;
		this.tokenRepository = tokenRepository;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<UserResponse> authenticationRequest(@Valid @RequestBody AuthUserRequest authUserRequest, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUserRequest.getEmail(), authUserRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final User user = (User) userDetailsService.loadUserByUsername(authUserRequest.getEmail());
		final String token = TokenUtils.generateToken();
		final String remoteAddress = request.getRemoteAddr();
		final String userAgent = request.getHeader("User-Agent");

		authTokenService.processToGnerateAuthToken(user, token, remoteAddress, userAgent, authUserRequest.isRememberMe());

		StringBuilder cookie = new StringBuilder();
		cookie.append(TokenUtils.TOKEN_HEADER);
		cookie.append("=").append(token);
		cookie.append("; Path=/;");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", cookie.toString());

		return new ResponseEntity<UserResponse>(new UserResponse(user), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/auth/logout", method = RequestMethod.GET)
	public ResponseEntity<?> logoutRequest(HttpServletRequest request, HttpServletResponse response) {

		final String token = request.getHeader(TokenUtils.TOKEN_HEADER);
		final String remoteAddress = request.getRemoteAddr();

		AuthToken existedAuthToken = null;
		if (!StringUtils.isEmpty(token))
			existedAuthToken = authTokenService.findFistByTokenAndRemoteAddress(token, remoteAddress);

		if (null == existedAuthToken) {
			log.error("Token[" + token + "] not found");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		authTokenService.delete(existedAuthToken);
		return ResponseEntity.ok().build();

	}

	@RequestMapping(value = "/auth/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> authenticationRequest(HttpServletRequest request, HttpServletResponse response) {

		String token = request.getHeader(TokenUtils.TOKEN_HEADER);

		AuthToken authToken = null;
		if (!StringUtils.isEmpty(token))
			authToken = tokenRepository.findFistByTokenAndRemoteAddress(token, request.getRemoteAddr());

		if (authToken == null) {
			log.error("Token[" + token + "] not found");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String newToken = TokenUtils.refreshToken();
		authToken.setToken(newToken);
		authTokenService.save(authToken);
		response.setHeader(TokenUtils.TOKEN_HEADER, newToken);
		return ResponseEntity.ok(new TokenResponse(newToken));

	}
}
