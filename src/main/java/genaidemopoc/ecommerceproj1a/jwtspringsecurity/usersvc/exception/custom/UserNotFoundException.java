package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String userId) {
		super(String.format(UserServiceConstants.USER_NOT_FOUND_WITH_ID, userId));
	}
	
	public UserNotFoundException() {
        super(UserServiceConstants.USER_NOT_FOUND);  
    }
}
