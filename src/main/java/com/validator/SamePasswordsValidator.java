package com.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.dto.CreateUserDTO;
import com.dto.UserDTO;

public class SamePasswordsValidator implements ConstraintValidator<SamePasswords, CreateUserDTO> {

	@Override
	public void initialize(SamePasswords constraintAnnotation) {
	}

	@Override
	public boolean isValid(CreateUserDTO value, ConstraintValidatorContext context) {
		if (value.getConfirmedPassword() == null) {
			return true;
		}

		return value.getConfirmedPassword().equals(value.getPassword());
	}

}
