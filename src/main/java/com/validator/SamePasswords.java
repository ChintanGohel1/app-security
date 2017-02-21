package com.validator;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ TYPE })
// 
@Retention(RUNTIME)
@Constraint(validatedBy = SamePasswordsValidator.class)
// validator
@Documented
public @interface SamePasswords {

	String message() default "password and confirm password do not match"; // default error message

	Class<?>[] groups() default {}; // required

	Class<? extends Payload>[] payload() default {}; // required
}
