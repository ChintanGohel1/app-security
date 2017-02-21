package com.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinit Solanki
 *
 */
public class ValidationError {

	//@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, String> errors = new HashMap<>();

	private String errorMessage;

	public ValidationError() {
		super();
	}

	public ValidationError(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ValidationError(Map<String, String> errors) {
		this.errors = errors;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
