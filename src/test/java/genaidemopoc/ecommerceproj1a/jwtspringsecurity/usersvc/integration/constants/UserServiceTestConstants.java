package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.constants;

/**
 * Constants used across user service integration tests.
 * Centralizes all test-related constants to ensure consistency and easier maintenance.
 */
public final class UserServiceTestConstants {
    
    private UserServiceTestConstants() {
        // Private constructor to prevent instantiation
    }
    
    // Test user details
    public static final String USER_EMAIL_PREFIX = "test.user";
    public static final String ADMIN_EMAIL_PREFIX = "test.admin";
    public static final String TEST_PASSWORD = "Test@123";
    public static final String TEST_USER_NAME = "Test User";
    public static final String TEST_ADMIN_NAME = "Test Admin";

    // Test roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // API endpoints
    public static final String API_AUTH_REGISTER = "/users/api/auth/user/register";
    public static final String API_AUTH_ADMIN_REGISTER = "/users/api/auth/admin/register";
    public static final String API_AUTH_LOGIN = "/users/api/auth/user/login";
    public static final String API_AUTH_ADMIN_LOGIN = "/users/api/auth/admin/login";
    public static final String API_AUTH_REFRESH = "/users/api/auth/refresh";
    public static final String API_AUTH_LOGOUT = "/users/api/auth/logout";

    // Test response messages
    public static final String MSG_USER_CREATED = "User created successfully";
    public static final String MSG_ADMIN_CREATED = "Admin created successfully";
    public static final String MSG_LOGIN_SUCCESS = "Login successful";
    public static final String MSG_LOGOUT_SUCCESS = "Logout successful";
    public static final String MSG_TOKEN_REFRESHED = "Token refreshed successfully";

    // Test error messages
    public static final String ERR_USER_EXISTS = "User already exists";
    public static final String ERR_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERR_TOKEN_INVALID = "Invalid token";
    
    // Common test data
    public static final String USER_ROLE = "USER";
    public static final String REFRESH_TEST_USER_NAME = "Refresh Test User";
    public static final String TOKEN_STORAGE_TEST_USER_NAME = "Token Storage Test User";
    public static final String PERFORMANCE_TEST_USER_PREFIX = "Performance Test User ";
    
    // URL & Endpoints
    public static final String API_AUTH_REFRESH_TOKEN = "/api/auth/user/refreshtoken";
    public static final String API_AUTH_LOGOUT_ALL = "/api/auth/user/logout-all";
    public static final String API_USERS_CURRENT = "/api/users/user/current";
    public static final String API_ADMIN_USERS = "/api/admin/user/all";
    public static final String API_ADMIN_USERS_SEARCH = "/admin/user/search";
    
    // Email prefixes for test users
    public static final String REFRESH_TEST_EMAIL_PREFIX = "refresh-test-";
    public static final String TOKEN_STORAGE_EMAIL_PREFIX = "token-storage-";
    public static final String PERF_TEST_EMAIL_PREFIX = "perf-test-";
    public static final String EMAIL_DOMAIN = "@test.com";
    
    // HTTP Headers
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String REFRESH_TOKEN_PARAM = "refreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // Cookie keys
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken=";
    
    // Response keys
    public static final String MESSAGE_KEY = "message";
    public static final String MSG_KEY = "msg";
    public static final String SUCCESS_KEY = "success";
    public static final String REGISTERED_KEY = "registered";
    
    // Performance testing constants
    public static final int CONCURRENT_USERS = 10;
    public static final int OPERATIONS_PER_USER = 5;
    public static final long MAX_TOKEN_GEN_TIME_MS = 500;
    public static final int REFRESH_ATTEMPTS = 5;
    
    // Test configuration
    public static final long API_TIMEOUT_SECONDS = 60;
} 