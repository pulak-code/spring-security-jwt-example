package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;

public class InvalidCredentialsException extends RuntimeException {
	private static final long serialVersionUID = 5L;

	public InvalidCredentialsException(String format) {
		super(String.format(AppConstants.INVALID_CREDENTIALS_WITH_EMAIL, format));
	}
	
	public InvalidCredentialsException() {
        super(AppConstants.INVALID_CREDENTIALS);  
    }
}
