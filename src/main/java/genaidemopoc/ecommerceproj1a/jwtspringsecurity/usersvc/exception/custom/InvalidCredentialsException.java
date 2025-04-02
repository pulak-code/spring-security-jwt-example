package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class InvalidCredentialsException extends RuntimeException {
	private static final long serialVersionUID = 5L;

	public InvalidCredentialsException(String format) {
		super(String.format(UserServiceConstants.INVALID_CREDENTIALS_WITH_EMAIL, format));
	}
	
	public InvalidCredentialsException() {
        super(UserServiceConstants.INVALID_CREDENTIALS);  
    }
	
}
