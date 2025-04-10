package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // Or another appropriate status
public class InvalidRefreshTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Example serialVersionUID

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
} 