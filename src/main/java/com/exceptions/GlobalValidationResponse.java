package com.exceptions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.entities.AuthToken;
import com.google.common.base.Splitter;
import com.mail.SmtpMailSender;
import com.services.AuthTokenService;
import com.util.TokenUtils;

/**
 * @author Vinit Solanki
 *
 */
@ControllerAdvice
public class GlobalValidationResponse extends ResponseEntityExceptionHandler {

	@Autowired
	private SmtpMailSender smtpMailSender;

	@Autowired
	private AuthTokenService authTokenService;

	HashMap<String, String> responseMessage = new HashMap<String, String>();

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ValidationError error = ValidationErrorBuilder.fromBindingErrors(exception.getBindingResult());
		return super.handleExceptionInternal(exception, error, headers, status, request);
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<?> handleMaxUploadException(MultipartException ex) {

		if (ex.getCause() != null && ex.getCause().getCause() instanceof SizeException) {
			//responseMessage.put("message", "The request was rejected because its size exceeds the configured maximum");
			return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body(new ValidationError("The request was rejected because its size exceeds the configured maximum"));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

	}

	@ExceptionHandler({ BadCredentialsException.class, InternalAuthenticationServiceException.class })
	public ResponseEntity<?> handleBadCredentialsException(Exception ex) {
		//responseMessage.put("message", "username or password not missmatch");

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ValidationError("username or password not match"));

	}

	@ExceptionHandler(AlreadyExist.class)
	public ResponseEntity<?> handleAlreadyExistException(AlreadyExist ex, HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ValidationError(Splitter.on(",").withKeyValueSeparator(":").split(ex.getMessage())));

	}

	@ExceptionHandler(EntityNotFound.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFound ex, HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ValidationError(ex.getMessage()));

	}

	@ExceptionHandler(EmptyFileException.class)
	public ResponseEntity<?> handleEmptyFileException(EmptyFileException ex, HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ValidationError(ex.getMessage()));

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request, HttpServletResponse response) {

		responseMessage.put("message", "Unauthorized");

		//comment this code if you not required specific reason for the unauthorized.
		String token = request.getHeader(TokenUtils.TOKEN_HEADER);
		if (!StringUtils.isEmpty(token))
		{
			AuthToken authToken = authTokenService.findFistByTokenAndRemoteAddress(token, request.getRemoteAddr());
			if (null != authToken && TokenUtils.isTokenExpired(authToken)) {

				responseMessage.put("message", "token expired");

			}
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ValidationError(responseMessage));

	}

	@ExceptionHandler(DisabledException.class)
	@ResponseStatus(code = HttpStatus.LOCKED, reason = "Account disabled or locked")
	public void userDisabledException(DisabledException ex) {
	}

}
