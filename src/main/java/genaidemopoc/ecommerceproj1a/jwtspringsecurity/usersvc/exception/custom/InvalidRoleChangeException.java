package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

public class InvalidRoleChangeException extends RuntimeException {

	private static final long serialVersionUID = -7407363396059672961L;

	public InvalidRoleChangeException(String msg) {
		super(msg);
	}

}
