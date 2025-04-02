package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

public final class LoggingUserServiceConstants {
    private LoggingUserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // Authentication Logging
    public static final String LOG_AUTH_SUCCESS = "Authentication successful for user: {}";
    public static final String LOG_AUTH_FAILURE = "Authentication failed for user: {}";
    public static final String LOG_TOKEN_VALIDATION = "Validating token for user: {}";
    public static final String LOG_TOKEN_VALID = "Token validation successful for user: {}";
    public static final String LOG_TOKEN_INVALID = "Token validation failed for user: {}";
    public static final String LOG_TOKEN_EXPIRED = "Token expired for user: {}";

    // User Management Logging
    public static final String LOG_USER_CREATED = "User created successfully: {}";
    public static final String LOG_USER_UPDATED = "User updated successfully: {}";
    public static final String LOG_USER_DELETED = "User deleted successfully: {}";
    public static final String LOG_USER_NOT_FOUND = "User not found: {}";
    public static final String LOG_USER_EXISTS = "User already exists: {}";

    // Request Logging
    public static final String LOG_REQUEST_START = "Request started: {} {}";
    public static final String LOG_REQUEST_END = "Request completed: {} {}";
    public static final String LOG_REQUEST_ERROR = "Request failed: {} {}";

    // Security Logging
    public static final String LOG_ACCESS_DENIED = "Access denied for user: {}";
    public static final String LOG_INVALID_CREDENTIALS = "Invalid credentials for user: {}";
    public static final String LOG_ROLE_CHECK = "Checking role for user: {}";

    // Token Management Logging
    public static final String LOG_TOKEN_GENERATED = "Token generated for user: {}";
    public static final String LOG_TOKEN_REFRESHED = "Token refreshed for user: {}";
    public static final String LOG_TOKEN_BLACKLISTED = "Token blacklisted for user: {}";
    public static final String LOG_TOKEN_REMOVED = "Token removed from blacklist for user: {}";

    // Error Logging
    public static final String LOG_ERROR = "Error occurred: {}";
    public static final String LOG_EXCEPTION = "Exception occurred: {}";
    public static final String LOG_VALIDATION_ERROR = "Validation error: {}";

    // Performance Logging
    public static final String LOG_PERFORMANCE = "Performance metric: {}";
} 