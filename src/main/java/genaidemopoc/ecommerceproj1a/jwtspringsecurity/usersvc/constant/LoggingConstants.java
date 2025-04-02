package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Merged constants for logging messages used throughout the application.
 * Centralizes all logging-related constants to ensure consistency and easier maintenance.
 */
public final class LoggingConstants {
    
    private LoggingConstants() {
        // Private constructor to prevent instantiation
    }
    
    // General Info
    public static final String STARTING_APPLICATION = "Starting application: {}";
    public static final String APPLICATION_STARTED = "Application {} started successfully.";
    
    // Service Layer - General
    public static final String REGISTER_NEW_USER = "Registering new user: {}";
    public static final String USER_REGISTERED = "User registered successfully: {}";
    public static final String LOGGING_AND_GENERATING_TOKEN = "Logging the existing user: {} and generating the authentication token";
    
    // User Service Messages
    public static final String DELETING_USER = "Deleting user with id: {}";
    public static final String USER_DELETED = "User with id: {} deleted successfully";
    public static final String ADMIN_UPDATING_USER = "Admin updating user with id: {}";
    public static final String USER_UPDATED_BY_ADMIN = "User with id: {} updated successfully by admin";
    public static final String USER_EDITING_OWN_DETAILS = "User {} editing their own details";
    public static final String USER_UPDATED_OWN_DETAILS = "User {} updated their details successfully";
    public static final String FINDING_USER_BY_EMAIL = "Finding user by email";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: {}";
    
    // Admin Service Messages
    public static final String SEARCHING_ADMINS = "Searching admins by email: {} or adminname: {}";
    public static final String DELETING_ALL_USERS = "Deleting all users except current admin";
    public static final String ALL_USERS_DELETED = "All users deleted successfully except current admin: {}";
    public static final String FETCHING_ALL_USERS = "Fetching all users";
    
    // JWT Utility Messages
    public static final String GENERATING_TOKEN_FOR_USER = "Generating JWT token for user: {}";
    public static final String TOKEN_GENERATED_FOR_USER = "JWT token generated for user: {}";
    public static final String VALIDATING_TOKEN_FOR_USER = "Validating JWT token for user: {}";
    public static final String TOKEN_VALID_FOR_USER = "JWT token is valid for user: {}";
    public static final String TOKEN_INVALID_FOR_USER = "Invalid or expired JWT token for user: {}";
    public static final String JWT_PARSING_ERROR = "Critical error while parsing JWT: {}";
    public static final String JWT_EXPIRATION_CHECK_FAILED = "Failed to check token expiry: {}";
    public static final String JWT_TOKEN_IS_EXPIRED = "JWT token is expired";
    public static final String TOKEN_VALIDATION_FAILED_FOR_USER = "Token validation failed for user: {}";
    public static final String INVALID_JWT_TOKEN = "Invalid or Malformed JWT token";
    
    // Auth Service Messages
    public static final String ACCESS_TOKEN_BLACKLISTED = "Access token blacklisted successfully.";
    public static final String REFRESH_TOKEN_BLACKLISTED = "Refresh token blacklisted for user: {}";
    public static final String USER_LOGGED_OUT_ALL_DEVICES = "User '{}' logged out from all devices.";
    public static final String LOGOUT_ALL_DEVICES_ERROR = "Error during logout from all devices: {}";
    public static final String GENERATING_REFRESH_TOKEN = "Generating refresh token for user: {}";
    public static final String REFRESH_TOKEN_GENERATED = "Refresh token generated for user: {}";
    public static final String REFRESH_TOKEN_INVALID = "Invalid or expired refresh token for user: {}";
    public static final String REFRESH_TOKEN_VALIDATION_FAILED = "Refresh token validation failed: {}";
    public static final String GENERATING_REFRESH_TOKEN_FOR_USER = "Generating refresh token for user: {}";
    
    // Authentication Filter Messages
    public static final String TOKEN_EXTRACTED_FROM_HEADER = "JWT token extracted from Authorization header for user: {}";
    public static final String USER_AUTHENTICATED_SUCCESSFULLY = "User {} authenticated successfully.";
    public static final String AUTHENTICATION_FAILED_FOR_USER = "Authentication failed for user: {}";
    public static final String JWT_FILTER_ERROR = "Error processing JWT authentication: {}";
    public static final String TOKEN_VALIDATION_FAILED = "Token validation failed for user: {}";
    
    // CustomUserDetailsService Messages
    public static final String FETCHING_USER_DETAILS = "Fetching user details for email: {}";
    public static final String USER_NOT_FOUND = "User not found: {}";
    public static final String USER_DETAILS_LOADED = "User details loaded successfully for email: {}";
    public static final String USER_FOUND = "User found: {}";
    
    // Security Config Messages
    public static final String SECURITY_CONFIG_INITIALIZED = "Security configuration initialized.";
    public static final String SECURITY_FILTER_CHAIN_CONFIGURED = "Security filter chain configured.";
    
    // CORS Filter Messages
    public static final String CORS_FILTER_APPLIED = "CORS filter applied to request: {}";
    public static final String CORS_OPTIONS_REQUEST_HANDLED = "CORS preflight request handled successfully.";
    public static final String REJECTING_BLACKLISTED_TOKEN = "Blacklisted token detected, rejecting request.";
    
    // Token Blacklist Service Messages
    public static final String TOKEN_BLACKLISTED = "Blacklisted token for user: {}";
    public static final String TOKEN_ADDED_TO_BLACKLIST = "Token added to blacklist successfully";
    public static final String TOKEN_BLACKLIST_CHECK = "Checking if token is blacklisted: {}";
    public static final String TOKEN_IS_BLACKLISTED = "Token is blacklisted: {}";
    public static final String TOKEN_IS_NOT_BLACKLISTED = "Token is not blacklisted: {}";
    public static final String TOKEN_BLACKLIST_ERROR = "Error checking token blacklist: {}";
    
    // Token Storage Service Messages
    public static final String TOKEN_STORAGE_STRATEGY_INITIALIZED = "Token storage strategy initialized to: {}";
    public static final String INVALID_TOKEN_STORAGE_STRATEGY = "Invalid token storage strategy: {}. Defaulting to MEMORY";
    public static final String SETTING_TOKEN_STORAGE_STRATEGY = "Setting token storage strategy to: {}";
    public static final String ACCESS_TOKEN_STORED_MEMORY = "Access token stored in memory";
    public static final String ACCESS_TOKEN_STORED_ENV = "Access token stored in environment variable";
    public static final String ACCESS_TOKEN_STORED_FILE = "Access token stored in properties file";
    public static final String REFRESH_TOKEN_STORED_MEMORY = "Refresh token stored in memory";
    public static final String REFRESH_TOKEN_STORED_ENV = "Refresh token stored in environment variable";
    public static final String REFRESH_TOKEN_STORED_FILE = "Refresh token stored in properties file";
    public static final String TOKENS_CLEARED_MEMORY = "Tokens cleared from memory";
    public static final String TOKENS_CLEARED_ENV = "Tokens cleared from environment variables";
    public static final String TOKENS_CLEARED_FILE = "Tokens cleared from properties file";
    
    // Exception Messages
    public static final String SECURITY_EXCEPTION = "Security exception occurred: {}";
    public static final String DATABASE_EXCEPTION = "Database exception occurred: {}";
    public static final String VALIDATION_EXCEPTION = "Validation exception occurred: {}";
    public static final String GENERAL_EXCEPTION = "Exception occurred: {}";
    public static final String API_EXCEPTION = "API exception occurred: {}";
    
    // Performance Messages
    public static final String PERFORMANCE_OPERATION_START = "Starting operation: {}";
    public static final String PERFORMANCE_OPERATION_END = "Completed operation: {} in {} ms";
    public static final String PERFORMANCE_BOTTLENECK = "Performance bottleneck detected in operation: {}";
    
    // Security Messages
    public static final String SECURITY_ACCESS_DENIED = "Access denied for user: {} to resource: {}";
    public static final String SECURITY_SUSPICIOUS_ACTIVITY = "Suspicious activity detected from IP: {} for user: {}";
    public static final String SECURITY_MULTIPLE_FAILED_LOGINS = "Multiple failed login attempts detected for user: {}";
    
    // Service Health Messages
    public static final String SERVICE_STARTUP = "Service started successfully";
    public static final String SERVICE_SHUTDOWN = "Service shutting down";
    public static final String SERVICE_HEALTH_CHECK = "Health check performed with status: {}";
    public static final String SERVICE_COMPONENT_STATUS = "Component {} status: {}";
    
    // Repository & Database Messages
    public static final String DB_QUERY_EXECUTED = "Database query executed: {}";
    public static final String DB_INSERT_SUCCESS = "Database insert successful for entity: {}";
    public static final String DB_UPDATE_SUCCESS = "Database update successful for entity: {}";
    public static final String DB_DELETE_SUCCESS = "Database delete successful for entity: {}";
    public static final String DB_QUERY_ERROR = "Database query error: {}";
    public static final String DB_CONNECTION_ERROR = "Database connection error: {}";
    
    // API Request/Response Messages
    public static final String API_REQUEST_RECEIVED = "API request received: {} {}";
    public static final String API_RESPONSE_SENT = "API response sent: {} with status: {}";
    public static final String API_REQUEST_VALIDATION_FAILED = "API request validation failed: {}";
    public static final String API_RATE_LIMIT_EXCEEDED = "API rate limit exceeded for IP: {}";
} 