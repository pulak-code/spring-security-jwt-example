package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class UserAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 2L;

	public UserAlreadyExistsException(String format) {
		super(String.format(UserServiceConstants.USER_ALREADY_EXISTS, format));
	}
	
	public UserAlreadyExistsException() {
        super(UserServiceConstants.USER_ALREADY_EXISTS);  
    }
	
}
