package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants for user-facing messages used throughout the application.
 * Centralizes all message text to ensure consistency and easier maintenance.
 */
public final class MessageConstants {
    
    private MessageConstants() {
        // Private constructor to prevent instantiation
    }
    
    // Success messages
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully";
    public static final String USER_LOGIN_SUCCESS = "User logged in successfully";
    public static final String USER_LOGOUT_SUCCESS = "User logged out successfully";
    public static final String USER_LOGOUT_ALL_SUCCESS = "User logged out from all devices successfully";
    public static final String USER_UPDATED_SUCCESS = "User details updated successfully";
    public static final String USER_DELETED_SUCCESS = "User deleted successfully";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset instructions sent to your email";
    public static final String TOKEN_REFRESHED_SUCCESS = "Token refreshed successfully";
    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String LOGGED_OUT_SUCCESSFULLY = "User logged out successfully";
    
    // Error messages - User management
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with ID: %s";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email: %s";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_EMAIL_ALREADY_EXISTS = "User with email %s already exists";
    public static final String USER_ALREADY_EXISTS_WITH_EMAIL = "User with email %s already exists";
    public static final String FAILED_TO_REGISTER_USER = "Failed to register user";
    
    // Error messages - Input validation
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_IS_REQUIRED = "Email is required";
    public static final String EMAIL_EMPTY = "Email cannot be empty";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String EMAIL_INVALID_WITH_VALUE = "Invalid email format: %s";
    public static final String INVALID_EMAIL = "Invalid Email";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format %s";
    
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_IS_REQUIRED = "Password is required";
    public static final String PASSWORD_EMPTY = "Password cannot be empty";
    public static final String PASSWORD_TOO_WEAK = "Password is too weak";
    public static final String WEAK_PASSWORD_PROVIDED = "Weak Password";
    public static final String WEAK_PASSWORD_PROVIDED_WITH_PASSWORD = "Password %s does not meet strength requirements";
    public static final String PASSWORD_REQUIREMENTS = "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character";
    public static final String OLD_PASSWORD_INCORRECT = "Current password is incorrect";
    public static final String PASSWORDS_DONT_MATCH = "New password and confirm password don't match";
    
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_IS_REQUIRED = "Name is required";
    
    // Error messages - Authentication
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String INVALID_CREDENTIALS_WITH_EMAIL = "Invalid Credentials %s Provided";
    public static final String ACCOUNT_LOCKED = "Account is locked. Please contact support";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String ACCESS_DENIED = "Access denied. Insufficient permissions";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_MISSING = "Authentication token is missing";
    public static final String TOKEN_BLACKLISTED = "Token has been revoked";
    public static final String REFRESH_TOKEN_INVALID = "Invalid refresh token";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired. Please login again";
    public static final String REFRESH_TOKEN_MISSING = "Refresh token is missing";
    public static final String TOKEN_GEN_FAILED = "Token generation not successful";
    public static final String ERROR_GENERATING_KEY = "Error generating Key";
    public static final String SECRET_KEY_UPDATED = "Updated Secret Key";
    
    // Error messages - General
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String BAD_REQUEST = "Invalid request";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String NOT_FOUND = "Resource not found";
    public static final String METHOD_NOT_ALLOWED = "Method not allowed";
    public static final String CONFLICT = "Conflict";
    public static final String SERVICE_UNAVAILABLE = "Service temporarily unavailable";
    
    // Notification messages
    public static final String PASSWORD_EXPIRY_WARNING = "Your password will expire in %s days. Please update it.";
    public static final String ACCOUNT_INACTIVE_WARNING = "Your account has been inactive for %s days.";
    public static final String SESSION_TIMEOUT_WARNING = "Your session will expire in %s minutes.";
    public static final String NEW_DEVICE_LOGIN = "New login detected from device: %s, location: %s.";
    
    // API documentation messages
    public static final String API_REGISTER_USER_DESC = "Registers a new user";
    public static final String API_LOGIN_USER_DESC = "Authenticates a user and returns JWT token";
    public static final String API_REFRESH_TOKEN_DESC = "Refreshes the access token using a refresh token";
    public static final String API_LOGOUT_DESC = "Logs out the current user";
    public static final String API_LOGOUT_ALL_DESC = "Logs out the user from all devices";
    public static final String API_GET_USER_DESC = "Retrieves user details";
    public static final String API_UPDATE_USER_DESC = "Updates user details";
    public static final String API_DELETE_USER_DESC = "Deletes a user";
    public static final String API_SEARCH_USERS_DESC = "Searches for users by criteria";
    public static final String API_RESET_PASSWORD_DESC = "Sends password reset instructions";
    public static final String API_CHANGE_PASSWORD_DESC = "Changes user password";
    public static final String CONTROLLER_DESCRIPTION = "API for managing users";
    public static final String USER_CONTROLLER = "User Controller";
    public static final String USER_LOGIN = "User Login";
    public static final String REGISTERS_A_NEW_USER = "Registers a new User";
    public static final String REGISTER_USER = "Register User";
    public static final String FETCH_A_USER_USING_THEIR_ID = "Fetch a user using their ID";
    public static final String GET_USER_BY_ID = "Get user by ID";
    public static final String SEARCHING_BY_EMAIL = "Searching for user by email: {}";
    public static final String AUTHENTICATES_A_USER_AND_RETURNS_JWT_TOKEN = "Authenticates a user and returns JWT token";
    
    // API Documentation
    public static final String API_TITLE = "User Service E-Commerce API";
    public static final String API_DESCRIPTION = "API documentation for E-Commerce Users microservices";
    public static final String API_VERSION = "1.0";
    public static final String JWT = "JWT";
    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";
} 