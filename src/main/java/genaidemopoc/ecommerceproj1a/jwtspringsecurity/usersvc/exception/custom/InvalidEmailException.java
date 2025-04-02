package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class InvalidEmailException extends RuntimeException {

	private static final long serialVersionUID = 7L;
	public InvalidEmailException(String format) {
		super(String.format(UserServiceConstants.INVALID_EMAIL_FORMAT, format));
	}
	
	public InvalidEmailException() {
        super(UserServiceConstants.INVALID_EMAIL);  
    }
}
