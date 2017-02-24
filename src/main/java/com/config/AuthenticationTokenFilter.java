package com.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import com.entity.AuthToken;
import com.services.AuthTokenService;
import com.util.Constants;
import com.util.TokenUtils;

/**
 * @author Vinit Solanki
 *
 */
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private AuthTokenService authTokenService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		long startTime = System.currentTimeMillis();

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		//System.out.println("in Filter | " + httpRequest.getRequestURI());

		String token = httpRequest.getHeader(TokenUtils.TOKEN_HEADER);

		httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:8181");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " + TokenUtils.TOKEN_HEADER);

		if (StringUtils.hasText(token)) {

			//TODO : need to add token on cache and verfiy token on cache, If cache dont have token then only check token on database.
			AuthToken authToken = authTokenService.findFistByTokenAndRemoteAddress(token, httpRequest.getRemoteAddr());

			if (TokenUtils.isValidAuthToken(authToken) && null == SecurityContextHolder.getContext().getAuthentication()) {

				if (!TokenUtils.isTokenExpired(authToken)) {

					UserDetails userDetails = authToken.getUser();

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
					SecurityContextHolder.getContext().setAuthentication(authentication);

					// Refresh Token : If Required to refresh token on each request otherwise comment this code.
//					if (!httpRequest.getRequestURI().equals(Constants.LOGOUT_URL))
//					{
//						token = TokenUtils.refreshToken();
//						authToken.setToken(token);
//						authToken.setUpdatedOn(new Date());
//						authTokenService.save(authToken);
//
//						Cookie cookie = new Cookie(TokenUtils.TOKEN_HEADER, token); // Not necessary, but saves bandwidth.
//						cookie.setPath("/");
//						cookie.setMaxAge(TokenUtils.TOKEN_EXPIRATION_TIME.intValue());
//						cookie.setDomain("localhost");
//						httpResponse.addCookie(cookie);
//					}
				}
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Execute time : " + (startTime - endTime));
		//ServletResponse servletResponse = (ServletResponse) httpResponse;
		//ServletRequest servletRequest = (ServletRequest) httpRequest;
		chain.doFilter(request, response);

	}

}