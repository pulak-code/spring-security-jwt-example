package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants;

public final class LoggingConstants {
    private LoggingConstants() {
        // Private constructor to prevent instantiation
    }

    // Authentication Logging
    public static final String LOG_AUTH_SUCCESS = "Authentication successful for user: {}";
    public static final String LOG_AUTH_FAILURE = "Authentication failed for user: {}";
    public static final String LOG_TOKEN_VALIDATION = "Validating token for user: {}";
    public static final String LOG_TOKEN_VALID = "Token validation successful for user: {}";
    public static final String LOG_TOKEN_INVALID = "Token validation failed for user: {}";
    public static final String LOG_TOKEN_EXPIRED = "Token expired for user: {}";
    public static final String LOG_AUTH_ATTEMPT = "Authentication attempt for user: {}";
    public static final String LOG_AUTH_PROCESS = "Processing authentication for user: {}";

    // User Management Logging
    public static final String LOG_USER_CREATED = "User created successfully: {}";
    public static final String LOG_USER_UPDATED = "User updated successfully: {}";
    public static final String LOG_USER_DELETED = "User deleted successfully: {}";
    public static final String LOG_USER_NOT_FOUND = "User not found: {}";
    public static final String LOG_USER_EXISTS = "User already exists: {}";
    public static final String LOG_USER_SEARCH = "Searching for user with criteria: {}";
    public static final String LOG_USER_VALIDATION = "Validating user data: {}";
    public static final String LOG_USER_ROLE_UPDATE = "Updating user role: {}";

    // Request Logging
    public static final String LOG_REQUEST_START = "Request started: {} {}";
    public static final String LOG_REQUEST_END = "Request completed: {} {}";
    public static final String LOG_REQUEST_ERROR = "Request failed: {} {}";
    public static final String LOG_REQUEST_PARAMS = "Request parameters: {}";
    public static final String LOG_REQUEST_PROCESSING = "Processing request: {}";
    public static final String LOG_RESPONSE_PREPARING = "Preparing response: {}";

    // Security Logging
    public static final String LOG_ACCESS_DENIED = "Access denied for user: {}";
    public static final String LOG_INVALID_CREDENTIALS = "Invalid credentials for user: {}";
    public static final String LOG_ROLE_CHECK = "Checking role for user: {}";
    public static final String LOG_SECURITY_CHECK = "Performing security check: {}";
    public static final String LOG_PERMISSION_CHECK = "Checking permissions for: {}";

    // Token Management Logging
    public static final String LOG_TOKEN_GENERATED = "Token generated for user: {}";
    public static final String LOG_TOKEN_REFRESHED = "Token refreshed for user: {}";
    public static final String LOG_TOKEN_BLACKLISTED = "Token blacklisted for user: {}";
    public static final String LOG_TOKEN_REMOVED = "Token removed from blacklist for user: {}";
    public static final String LOG_TOKEN_CREATION = "Creating new token for user: {}";
    public static final String LOG_TOKEN_VERIFICATION = "Verifying token authenticity: {}";

    // Error Logging
    public static final String LOG_ERROR = "Error occurred: {}";
    public static final String LOG_EXCEPTION = "Exception occurred: {}";
    public static final String LOG_VALIDATION_ERROR = "Validation error: {}";
    public static final String LOG_SYSTEM_ERROR = "System error occurred: {}";
    public static final String LOG_DATA_ERROR = "Data processing error: {}";

    // Performance Logging
    public static final String LOG_PERFORMANCE = "Performance metric: {}";
    public static final String LOG_EXECUTION_TIME = "Operation execution time: {} ms";
    public static final String LOG_RESOURCE_USAGE = "Resource usage: {}";

    // Service Operation Logging
    public static final String LOG_SERVICE_START = "Service operation started: {}";
    public static final String LOG_SERVICE_END = "Service operation completed: {}";
    public static final String LOG_SERVICE_ERROR = "Service operation failed: {}";
    public static final String LOG_METHOD_ENTRY = "Entering method: {}";
    public static final String LOG_METHOD_EXIT = "Exiting method: {}";
} 