package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;

public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 2L;

	public UserAlreadyExistsException(String email) {
		super(String.format(AppConstants.USER_EMAIL_ALREADY_EXISTS, email));
	}
	
	public UserAlreadyExistsException() {
        super(AppConstants.USER_ALREADY_EXISTS);  
    }
	
}
