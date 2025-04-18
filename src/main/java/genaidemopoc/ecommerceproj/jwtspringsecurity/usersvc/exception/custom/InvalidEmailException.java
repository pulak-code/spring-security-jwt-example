package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.ValidationConstants;

public class InvalidEmailException extends RuntimeException {

	private static final long serialVersionUID = 7L;
	public InvalidEmailException(String email) {
		super(String.format(ValidationConstants.EMAIL_INVALID_FORMAT, email));
	}
	
	public InvalidEmailException() {
        super(ValidationConstants.EMAIL_INVALID);
    }
}
