package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class WeakPasswordException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public WeakPasswordException(String format) {
		super(String.format(UserServiceConstants.WEAK_PASSWORD_PROVIDED_WITH_PASSWORD, format));
	}

	public WeakPasswordException() {
		super(UserServiceConstants.WEAK_PASSWORD_PROVIDED);
	}
}
