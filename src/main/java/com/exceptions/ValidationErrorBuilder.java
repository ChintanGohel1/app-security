package com.exceptions;

import java.util.HashMap;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.validator.SamePasswords;

/**
 * @author Vinit Solanki
 *
 */
public class ValidationErrorBuilder {

	public static ValidationError fromBindingErrors(Errors errors) {

		ValidationError validationError = new ValidationError("Validation failed. " + errors.getErrorCount() + " error(s)");
		HashMap<String, String> mapErrors = new HashMap<String, String>();

		for (FieldError objectError : errors.getFieldErrors()) {
			mapErrors.put(objectError.getField(), objectError.getDefaultMessage());
		}

		for (ObjectError objectError : errors.getGlobalErrors()) {

			if (objectError.getCode().equalsIgnoreCase(SamePasswords.class.getSimpleName()))
				mapErrors.put("confirmedPassword", objectError.getDefaultMessage());

		}

		validationError.setErrors(mapErrors);
		return validationError;
	}
}
