package com.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.dao.TokenDAO;
import com.dto.AuthUserDTO;
import com.dto.TokenDTO;
import com.dto.UserDTO;
import com.entities.AuthToken;
import com.entities.User;
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
	private TokenDAO tokenDao;

	public AuthenticationController() {
		super();
	}

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, AuthTokenService authTokenService, TokenDAO tokenDao) {
		super();
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.authTokenService = authTokenService;
		this.tokenDao = tokenDao;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> authenticationRequest(@Valid @RequestBody AuthUserDTO userVo, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userVo.getEmail(), userVo.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		final User user = (User) userDetailsService.loadUserByUsername(userVo.getEmail());
		final String token = TokenUtils.generateToken();
		final String remoteAddress = request.getRemoteAddr();
		final String userAgent = request.getHeader("User-Agent");

		authTokenService.processToGnerateAuthToken(user, token, remoteAddress, userAgent, userVo.isRememberMe());

		StringBuilder cookie = new StringBuilder();
		cookie.append(TokenUtils.TOKEN_HEADER);
		cookie.append("=").append(token);
		cookie.append("; Path=/;");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", cookie.toString());

		return new ResponseEntity<UserDTO>(new UserDTO(user), headers, HttpStatus.OK);
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
			authToken = tokenDao.findFistByTokenAndRemoteAddress(token, request.getRemoteAddr());

		if (authToken == null) {
			log.error("Token[" + token + "] not found");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String newToken = TokenUtils.refreshToken();
		authToken.setToken(newToken);
		authTokenService.save(authToken);
		response.setHeader(TokenUtils.TOKEN_HEADER, newToken);
		return ResponseEntity.ok(new TokenDTO(newToken));

	}
}
