package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom;

public class InvalidRoleChangeException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public InvalidRoleChangeException(String message) {
		super(message);
	}

}
