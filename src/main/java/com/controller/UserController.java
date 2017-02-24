package com.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.request.*;
import com.response.TokenResponse;
import com.response.UserResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import com.entity.PasswordResetToken;
import com.entity.User;
import com.exceptions.EntityNotFound;
import com.exceptions.ValidationError;
import com.mail.SmtpMailSender;
import com.services.AuthTokenService;
import com.services.PasswordResetTokenService;
import com.services.UserService;
import com.util.Constants;
import com.shared.TemplateService;
import com.util.TokenUtils;

/**
 * @author Vinit Solanki
 *
 **/
@RestController
@RequestMapping(value = "/user")
// @PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

	private static final Logger log = Logger.getLogger(UserController.class);

	private Constants constants;

	private UserService userService;

	private AuthTokenService authTokenService;

	private PasswordResetTokenService passwordResetTokenService;

	private SmtpMailSender smtpMailSender;

	public UserController() {
		super();
	}

	@Autowired
	public UserController(Constants constants, UserService userService, AuthTokenService authTokenService, PasswordResetTokenService passwordResetTokenService, SmtpMailSender smtpMailSender) {
		super();
		this.constants = constants;
		this.userService = userService;
		this.authTokenService = authTokenService;
		this.passwordResetTokenService = passwordResetTokenService;
		this.smtpMailSender = smtpMailSender;
	}

	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_LIST_GET')")
	public ResponseEntity<?> findAll() {

		List<UserResponse> users = userService.findAll();

		return ResponseEntity.ok(users);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('USER_POST')")
	public ResponseEntity<?> save(@Valid @RequestBody CreateUserRequest createUserRequest) {

		System.out.println("Save User");
		System.out.println("createUserRequest.getId() = " + createUserRequest.getId());
		System.out.println("createUserRequest.getFirstName() = " + createUserRequest.getFirstName());
		System.out.println("createUserRequest.getRole() = " + createUserRequest.getRole());

		UserResponse savedUserResponse = userService.save(createUserRequest);

		return ResponseEntity.ok(savedUserResponse);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('USER_GET')")
	//	@PreAuthorize("#contact.name == authentication.name")
	public ResponseEntity<?> findOne(@PathVariable("id") Long id) {

		UserResponse userResponse = userService.findOne(id);

		return ResponseEntity.ok(userResponse);

	}

	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('USER_PUT')")
	//	@PreAuthorize("#contact.name == authentication.name")
	public ResponseEntity<?> update(@PathVariable("id") Long id,
	        @Valid @RequestBody UserRequest userRequest) {

		if (id != userRequest.getId())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			        "user id mismatch");

		return ResponseEntity.ok(userService.update(userRequest));

	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('USER_DELETE')")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {

		userService.findOne(id); //check user exist or not

		userService.delete(id);

		return ResponseEntity.ok().build();

	}

	@RequestMapping(value = "/forgotPassword/{email:.+}", method = RequestMethod.POST)
	public ResponseEntity<?> forgotPassword(@PathVariable("email") String email, Locale locale) {
		HashMap<String, String> responseMessage = new HashMap();
		if (StringUtils.isEmpty(email)) {
			responseMessage.put("message", "email not found");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationError(responseMessage));
		}

		final UserResponse user = userService.findByEmail(email);

		final String token = TokenUtils.generateToken();

		passwordResetTokenService.save(new PasswordResetToken(token, new User(user)));

		try {

			String resetUrl = Constants.clientForgotPassUrl + email + "/" + token;
			final Context ctx = new Context(locale);
			ctx.setVariable("name", user.getFullName());
			ctx.setVariable("email", email);
			ctx.setVariable("action_url", resetUrl);
			ctx.setVariable("expiration_time", Duration.ofMillis(TokenUtils.RESET_PASSWORD_TOKEN_EXPIRATION_TIME).toMinutes());

			//final String htmlContent = this.templateEngine.process("resetpassword", ctx);

			smtpMailSender.send(user.getEmail(), "Reset Password" + token, TemplateService.getTemplate("resetpassword", ctx));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
		return ResponseEntity.ok().body("reset password link sent to your mail");

	}

	@RequestMapping(value = "/resetPassword/{token}", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@PathVariable("token") String token, @Valid @RequestBody AuthUserRequest authUserRequest, HttpServletRequest request, HttpServletResponse response) {

		if (StringUtils.isEmpty(authUserRequest.getEmail()) || StringUtils.isEmpty(token) || StringUtils.isEmpty(authUserRequest.getPassword()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		final User user = userService.loadUserByUsername(authUserRequest.getEmail());

		final PasswordResetToken passwordResetToken = passwordResetTokenService.findPasswordResetToken(user, token);

		if (null == passwordResetToken)
		{
			throw new EntityNotFound("Password reset token not found");
		}
		if (!TokenUtils.isTokenExpired(passwordResetToken)) {

			//TODO: reset password of user, get new password, update password on database and process to login, and return token.

			user.setPassword(authUserRequest.getPassword());
			userService.save(new CreateUserRequest(user));

			final String generatedToken = TokenUtils.generateToken();

			//authTokenService.processToGnerateAuthToken(user, generatedToken, request.getRemoteAddr(), request.getHeader("User-Agent"));

			passwordResetTokenService.delete(passwordResetToken);

			return ResponseEntity.ok(new TokenResponse(generatedToken));

		} else {

			HashMap<String, String> responseMessage = new HashMap();
			responseMessage.put("message", "Reset password token Expired");

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ValidationError(responseMessage));
		}

	}

}
