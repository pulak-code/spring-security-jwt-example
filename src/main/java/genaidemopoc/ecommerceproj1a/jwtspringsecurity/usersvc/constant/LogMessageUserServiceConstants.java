package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants for user service logging messages.
 */
public final class LogMessageUserServiceConstants {
    private LogMessageUserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // Authentication logging
    public static final String LOG_AUTH_SUCCESS = "Authentication successful for user: {}";
    public static final String LOG_AUTH_FAILURE = "Authentication failed for user: {}";
    public static final String LOG_TOKEN_VALIDATION = "Validating token for user: {}";
    public static final String LOG_TOKEN_VALID = "Token validation successful for user: {}";
    public static final String LOG_TOKEN_INVALID = "Token validation failed for user: {}";
    public static final String LOG_TOKEN_EXPIRED = "Token expired for user: {}";

    // User management logging
    public static final String LOG_USER_CREATED = "User created successfully: {}";
    public static final String LOG_USER_UPDATED = "User updated successfully: {}";
    public static final String LOG_USER_DELETED = "User deleted successfully: {}";
    public static final String LOG_USER_NOT_FOUND = "User not found: {}";
    public static final String LOG_USER_EXISTS = "User already exists: {}";

    // Request logging
    public static final String LOG_REQUEST_START = "Processing request: {} {}";
    public static final String LOG_REQUEST_END = "Request completed: {} {}";
    public static final String LOG_REQUEST_ERROR = "Error processing request: {} {}";

    // Security logging
    public static final String LOG_ACCESS_DENIED = "Access denied for user: {}";
    public static final String LOG_INVALID_CREDENTIALS = "Invalid credentials for user: {}";
    public static final String LOG_ROLE_CHECK = "Checking role {} for user: {}";

    // Token management logging
    public static final String LOG_TOKEN_GENERATED = "Token generated for user: {}";
    public static final String LOG_TOKEN_REFRESHED = "Token refreshed for user: {}";
    public static final String LOG_TOKEN_BLACKLISTED = "Token blacklisted for user: {}";
    public static final String LOG_TOKEN_REMOVED = "Token removed for user: {}";

    // Error logging
    public static final String LOG_ERROR = "Error occurred: {}";
    public static final String LOG_EXCEPTION = "Exception occurred: {}";
    public static final String LOG_VALIDATION_ERROR = "Validation error: {}";
    public static final String LOG_DATABASE_ERROR = "Database error: {}";

    // Performance logging
    public static final String LOG_PERFORMANCE = "Operation completed in {} ms: {}";
    public static final String LOG_SLOW_OPERATION = "Slow operation detected: {} took {} ms";
} 