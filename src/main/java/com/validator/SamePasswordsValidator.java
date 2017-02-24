package com.validator;

import com.request.CreateUserRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SamePasswordsValidator implements ConstraintValidator<SamePasswords, CreateUserRequest> {

	@Override
	public void initialize(SamePasswords constraintAnnotation) {
	}

	@Override
	public boolean isValid(CreateUserRequest value, ConstraintValidatorContext context) {
		if (value.getConfirmedPassword() == null) {
			return true;
		}

		return value.getConfirmedPassword().equals(value.getPassword());
	}

}
