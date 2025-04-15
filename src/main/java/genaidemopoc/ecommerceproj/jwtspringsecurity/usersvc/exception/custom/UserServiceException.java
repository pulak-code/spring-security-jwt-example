package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom;

public class UserServiceException extends RuntimeException {
	private static final long serialVersionUID = 4L;

	public UserServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
