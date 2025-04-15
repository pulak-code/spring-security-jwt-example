package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom;

public class InvalidJWTTokenException extends RuntimeException {

	private static final long serialVersionUID = 99L;
	public InvalidJWTTokenException(String errorMsg) {
		super(errorMsg);
	}

}
