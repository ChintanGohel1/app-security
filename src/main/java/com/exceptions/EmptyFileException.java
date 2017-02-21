package com.exceptions;

/**
 * @author Vinit Solanki
 *
 */
public class EmptyFileException extends RuntimeException {

	private static final long serialVersionUID = 1407471794242000834L;

	public EmptyFileException() {
	}

	public EmptyFileException(String arg0) {
		super(arg0);
	}

	public EmptyFileException(Throwable arg0) {
		super(arg0);
	}

	public EmptyFileException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public EmptyFileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}