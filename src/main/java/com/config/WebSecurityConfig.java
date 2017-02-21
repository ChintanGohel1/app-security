package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.services.AuthTokenService;
import com.services.UserService;

/**
 * @author Vinit Solanki
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String[] permittedUrls() {
		return new String[] { "/", "/home", "/login", "/auth/logout" };
	}

	@Autowired
	private UserService userDetailsService;

	@Autowired
	private AuthTokenService authTokenService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		        .passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {

		AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
		return authenticationTokenFilter;

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().exceptionHandling()
		        //.authenticationEntryPoint(unauthorizedEntryPoint())
		        // .accessDeniedHandler(accessDeniedHandler())
		        .and()
		        .sessionManagement()
		        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		        .authorizeRequests()
		        .antMatchers(HttpMethod.OPTIONS, "/**")
		        .permitAll().antMatchers(permittedUrls()).permitAll()
		        .anyRequest().permitAll();

		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

	}

	//	@Bean
	//	public AuthenticationEntryPoint unauthorizedEntryPoint() {
	//		return (request, response, authException) -> {
	//
	//			String token = request.getHeader(TokenUtils.TOKEN_HEADER);
	//			if (!StringUtils.isEmpty(token))
	//			{
	//				AuthToken authToken = authTokenService.findFistByTokenAndRemoteAddress(token, request.getRemoteAddr());
	//				if (null != authToken && TokenUtils.isTokenExpired(authToken)) {
	//					response.setStatus(498);
	//					response.getOutputStream().println("token expired");
	//					return;
	//				}
	//			}
	//
	//			HashMap<String, String> responseMessage = new HashMap();
	//			responseMessage.put("message", "Username or Password not match");
	//
	//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, new ValidationError(responseMessage).toString());
	//		};
	//	}

	//	@Bean
	//	public AccessDeniedHandler accessDeniedHandler() {
	//		return (request, response, authException) -> response.sendError(
	//		        HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
	//	}

}