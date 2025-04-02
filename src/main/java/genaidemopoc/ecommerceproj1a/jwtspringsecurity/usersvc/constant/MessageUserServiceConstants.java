package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants for user service messages.
 */
public final class MessageUserServiceConstants {
    private MessageUserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // User-related messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_CREATED_SUCCESS = "User created successfully";
    public static final String USER_UPDATED_SUCCESS = "User updated successfully";
    public static final String USER_DELETED_SUCCESS = "User deleted successfully";

    // Authentication messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_BLACKLISTED = "Token has been blacklisted";
    public static final String REFRESH_TOKEN_INVALID = "Invalid refresh token";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired";
    public static final String REFRESH_TOKEN_BLACKLISTED = "Refresh token has been blacklisted";

    // Validation messages
    public static final String EMAIL_IS_REQUIRED = "Email is required";
    public static final String PASSWORD_IS_REQUIRED = "Password is required";
    public static final String NAME_IS_REQUIRED = "Name is required";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String WEAK_PASSWORD = "Password does not meet security requirements";

    // Error messages
    public static final String SERVICE_ERROR = "An error occurred in the service";
    public static final String DATABASE_ERROR = "Database error occurred";
    public static final String VALIDATION_ERROR = "Validation error occurred";
    public static final String AUTHENTICATION_ERROR = "Authentication error occurred";
    public static final String AUTHORIZATION_ERROR = "Authorization error occurred";

    // Security messages
    public static final String ACCESS_DENIED = "Access denied";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Forbidden access";
    public static final String INVALID_TOKEN_FORMAT = "Invalid token format";
    public static final String TOKEN_MISSING = "Token is missing";

    // API messages
    public static final String API_ERROR = "API error occurred";
    public static final String API_SUCCESS = "API request successful";
    public static final String API_BAD_REQUEST = "Bad request";
    public static final String API_NOT_FOUND = "Resource not found";
    public static final String API_SERVER_ERROR = "Internal server error";
} 