package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants;

/**
 * General application constants including messages, API info, and utility values.
 */
public final class AppConstants {
    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    // --- General Utility Constants ---
    public static final String UTILITY_CLASS = "Utility class";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FILE_LOCATION = "classpath:application.properties";
    // Note: Regexes moved to ValidationConstants

    // --- API Information --- 
    public static final String API_TITLE = "User Service E-Commerce API";
    public static final String API_DESCRIPTION = "API documentation for E-Commerce Users microservices";
    public static final String API_VERSION = "1.0";
    public static final String API_USERS_URL_ENDPOINT = "/api/users";
    public static final String AUTH_BASE_PATH = "/api/auth";
    public static final String USER_LOGIN_ENDPOINT = "/login";
    public static final String USER_ENDPOINT = "/user";
    public static final String CONTROLLER_DESCRIPTION = "API for managing users";
    public static final String USER_CONTROLLER = "User Controller";
    public static final String BEARER_AUTHENTICATION = "Bearer Authentication";
    public static final String JWT = "JWT";
    
    // --- Operation Descriptions (Swagger/Logging) --- 
    public static final String REGISTER_USER = "Register User";
    public static final String REGISTERS_A_NEW_USER = "Registers a new User";
    public static final String USER_LOGIN = "User Login";
    public static final String AUTHENTICATES_A_USER_AND_RETURNS_JWT_TOKEN = "Authenticates a user and returns JWT token";
    public static final String GET_USER_BY_ID = "Get user by ID";
    public static final String FETCH_A_USER_USING_THEIR_ID = "Fetch a user using their ID";
    public static final String SEARCHING_BY_EMAIL = "Searching for user by email: {}";
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

    // --- User Success Messages --- 
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully";
    public static final String USER_LOGIN_SUCCESS = "User logged in successfully";
    public static final String USER_LOGOUT_SUCCESS = "User logged out successfully";
    public static final String USER_LOGOUT_ALL_SUCCESS = "User logged out from all devices successfully";
    public static final String USER_UPDATED_SUCCESS = "User details updated successfully";
    public static final String USER_DELETED_SUCCESS = "User deleted successfully";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String PASSWORD_RESET_EMAIL_SENT = "Password reset instructions sent to your email";
    public static final String TOKEN_REFRESHED_SUCCESS = "Token refreshed successfully";

    // --- User Error Messages --- 
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with ID: %s";
    public static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email: %s";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_EMAIL_ALREADY_EXISTS = "User with email %s already exists";
    public static final String FAILED_TO_REGISTER_USER = "Failed to register user";
    public static final String OLD_PASSWORD_INCORRECT = "Current password is incorrect";
    public static final String PASSWORDS_DONT_MATCH = "New password and confirm password don't match";
    public static final String ACCOUNT_LOCKED = "Account is locked. Please contact support";
    public static final String ACCOUNT_DISABLED = "Account is disabled";
    public static final String WEAK_PASSWORD_PROVIDED = "Weak password provided";
    public static final String WEAK_PASSWORD_PROVIDED_WITH_PASSWORD = "Password %s does not meet strength requirements";
    public static final String EMAIL_ALREADY_EXISTS = "Email already registered";
    public static final String USERNAME_ALREADY_EXISTS = "Username already taken";

    // --- General Error Messages --- 
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String BAD_REQUEST = "Invalid request";
    public static final String VALIDATION_ERROR = "Validation error";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";
    public static final String NOT_FOUND = "Resource not found";
    public static final String METHOD_NOT_ALLOWED = "Method not allowed";
    public static final String CONFLICT = "Conflict";
    public static final String SERVICE_UNAVAILABLE = "Service temporarily unavailable";
    public static final String ERROR_GENERATING_KEY = "Error generating Key";
    public static final String TOKEN_GEN_FAILED = "Token generation not successful";
    public static final String SECRET_KEY_UPDATED = "Updated Secret Key";

    // --- Authentication Error Messages (subset, others in SecurityConstants) ---
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_CREDENTIALS_WITH_EMAIL = "Invalid Credentials %s Provided";
    public static final String REFRESH_TOKEN_INVALID = "Refresh token is invalid or expired";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired. Please login again";
    public static final String REFRESH_TOKEN_MISSING = "Refresh token is missing";

    // --- Notification Messages --- 
    public static final String PASSWORD_EXPIRY_WARNING = "Your password will expire in %s days. Please update it.";
    public static final String ACCOUNT_INACTIVE_WARNING = "Your account has been inactive for %s days.";
    public static final String SESSION_TIMEOUT_WARNING = "Your session will expire in %s minutes.";
    public static final String NEW_DEVICE_LOGIN = "New login detected from device: %s, location: %s.";

    // --- Deprecated/Removed/Moved --- 
    // BEARER1 = "bearer" -> Likely obsolete, use SecurityConstants.BEARER
    // API_VERSION = "/api/v1" -> Use specific paths like API_USERS_URL_ENDPOINT
    // ACCESS_CONTROL_* -> Moved to SecurityConstants
    // HTTP_METHODS/HTTP_HEADERS -> Moved/merged into SecurityConstants
    // EMAIL_REGEX / PASSWORD_REGEX -> Moved to ValidationConstants
    // KEYGEN_HASHALGO -> Moved to SecurityConstants
    // Authentication related errors/success messages -> Mostly moved to SecurityConstants
    // Validation specific messages (like REQUIRED, EMPTY, INVALID_FORMAT) -> Moved to ValidationConstants
}
