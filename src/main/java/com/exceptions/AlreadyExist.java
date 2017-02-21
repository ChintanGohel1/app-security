package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vinit Solanki
 *
 */
//@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExist extends RuntimeException {

	private static final long serialVersionUID = 6860635900360600628L;

	public AlreadyExist() {
    }

    public AlreadyExist(String arg0) {
        super(arg0);
    }

    public AlreadyExist(Throwable arg0) {
        super(arg0);
    }

    public AlreadyExist(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AlreadyExist(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}