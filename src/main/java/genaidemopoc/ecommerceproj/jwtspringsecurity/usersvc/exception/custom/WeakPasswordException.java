package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom;

public class WeakPasswordException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WeakPasswordException(String message) {
        super(message);
    }

    public WeakPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
} 