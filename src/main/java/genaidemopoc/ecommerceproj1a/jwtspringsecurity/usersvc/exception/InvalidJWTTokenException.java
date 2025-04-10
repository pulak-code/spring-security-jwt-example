package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception;

public class InvalidJWTTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidJWTTokenException(String message) {
        super(message);
    }

    public InvalidJWTTokenException(String message, Throwable cause) {
        super(message, cause);
    }
} 