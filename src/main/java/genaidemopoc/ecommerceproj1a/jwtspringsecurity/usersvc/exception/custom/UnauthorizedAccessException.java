package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Returns 403 Forbidden response
public class UnauthorizedAccessException extends RuntimeException {

	private static final long serialVersionUID = 181219408625880801L;

	public UnauthorizedAccessException(String message) {
		super(message);
	}
}
