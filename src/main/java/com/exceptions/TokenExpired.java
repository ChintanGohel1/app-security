package com.exceptions;


/**
 * @author Vinit Solanki
 *
 */
//@ResponseStatus(code=498)
public class TokenExpired extends RuntimeException {

	private static final long serialVersionUID = 6860635900360600628L;

	public TokenExpired() {
    }

    public TokenExpired(String arg0) {
        super(arg0);
    }

    public TokenExpired(Throwable arg0) {
        super(arg0);
    }

    public TokenExpired(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TokenExpired(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}