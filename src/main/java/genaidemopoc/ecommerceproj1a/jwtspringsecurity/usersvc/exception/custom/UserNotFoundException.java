package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String identifier) {
		super(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, identifier));
	}
	
	public UserNotFoundException() {
        super(AppConstants.USER_NOT_FOUND);  
    }
}
