package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants;

/**
 * Centralized constants for all testing purposes.
 * This class consolidates constants from:
 * - UserServiceTestConstants
 * - LogMessageConstants
 * - UserServiceTestUserServiceConstants
 */
public final class TestConstants {
    private TestConstants() {
        // Private constructor to prevent instantiation
    }

    // ===== Test User Data =====
    public static final String TEST_USER_EMAIL = "test" + System.currentTimeMillis() + "@example.com";
    public static final String TEST_USER_PASSWORD = "Test@123";
    public static final String TEST_USER_NAME = "Test User";
    public static final String TEST_ADMIN_EMAIL = "admin" + System.currentTimeMillis() + "@example.com";
    public static final String TEST_ADMIN_PASSWORD = "Admin@123";
    public static final String TEST_ADMIN_NAME = "Admin User";
    public static final String USER_EMAIL_PREFIX = "testuser";
    public static final String USER_EMAIL_DOMAIN = "@example.com";
    public static final String USER_PASSWORD = "P@ssw0rd";
    public static final String USER_NAME_PREFIX = "Test User";

    // ===== Test Role Data =====
    public static final String TEST_USER_ROLE = "ROLE_USER";
    public static final String TEST_ADMIN_ROLE = "ROLE_ADMIN";

    // ===== Test Token Data =====
    public static final String TEST_TOKEN = "test.jwt.token";
    public static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    public static final String TEST_INVALID_TOKEN = "invalid.token";
    public static final String TEST_EXPIRED_TOKEN = "expired.token";

    // ===== API Endpoints =====
    public static final String API_AUTH_REGISTER = "/api/auth/register";
    public static final String API_AUTH_LOGIN = "/api/auth/login";
    public static final String API_AUTH_LOGOUT = "/api/auth/logout";
    public static final String API_AUTH_REFRESH = "/api/auth/refresh";
    public static final String API_USER_PROFILE = "/api/user/profile";
    public static final String API_USER_UPDATE = "/api/user/update";
    public static final String API_ADMIN_DASHBOARD = "/api/admin/dashboard";
    public static final String API_ADMIN_USERS = "/admin/user/all";
    public static final String TEST_AUTH_ENDPOINT = "/api/auth/test";
    public static final String TEST_USER_ENDPOINT = "/api/user/test";
    public static final String TEST_ADMIN_ENDPOINT = "/api/admin/test";

    // ===== Test Response Messages =====
    public static final String RESPONSE_SUCCESS = "success";
    public static final String RESPONSE_FAILURE = "failure";
    public static final String RESPONSE_ERROR = "error";
    public static final String RESPONSE_INVALID = "invalid";
    public static final String TEST_SUCCESS_MESSAGE = "Test operation successful";
    public static final String TEST_ERROR_MESSAGE = "Test operation failed";
    public static final String TEST_VALIDATION_MESSAGE = "Test validation message";

    // ===== Test Log Messages =====
    public static final String SKIPPING_TEST = "Skipping test due to missing prerequisite: ";
    public static final String TEST_FAILED = "Test failed with error: ";
    public static final String TOKEN_MISSING = "Auth token is missing";
    public static final String TOKEN_INVALID = "Auth token is invalid";
    public static final String TOKEN_EXPIRED = "Auth token has expired";
    public static final String REQUEST_INVALID = "Request is invalid: ";
    public static final String VALIDATION_FAILED = "Validation failed: ";
    public static final String TEST_SETUP_FAILED = "Test setup failed: ";
    public static final String TEST_CLEANUP_FAILED = "Test cleanup failed: ";

    // ===== Performance Testing =====
    public static final int CONCURRENT_USERS = 10;
    public static final int REQUESTS_PER_USER = 5;
    public static final int EXPECTED_RESPONSE_TIME_MS = 200;
    public static final long PERFORMANCE_TEST_DURATION = 60000; // 60 seconds
    
    // ===== Test Data Ranges =====
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;

    // ===== API Endpoint Details =====
    public static final String API_AUTH_USER_REGISTER = "/api/auth/user/register";
    public static final String API_AUTH_ADMIN_REGISTER = "/api/auth/admin/register";
    public static final String API_AUTH_USER_LOGIN = "/api/auth/user/login";
    public static final String API_AUTH_ADMIN_LOGIN = "/api/auth/admin/login";
    public static final String API_AUTH_REFRESH_TOKEN = "/api/auth/user/refreshtoken";
    public static final String API_AUTH_LOGOUT_ALL = "/api/auth/user/logout-all";
    public static final String API_USERS_CURRENT = "/api/users/user/current";
    public static final String API_ADMIN_USERS_SEARCH = "/admin/user/search";
    
    // ===== HTTP Headers and Parameters =====
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String REFRESH_TOKEN_PARAM = "refreshToken";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // ===== Response and Message Keys =====
    public static final String MESSAGE_KEY = "message";
    public static final String MSG_KEY = "msg";
    public static final String SUCCESS_KEY = "success";
    public static final String REGISTERED_KEY = "registered";
    
    // ===== Success Messages =====
    public static final String MSG_USER_CREATED = "User created successfully";
    public static final String MSG_ADMIN_CREATED = "Admin created successfully";
    public static final String MSG_LOGIN_SUCCESS = "Login successful";
    public static final String MSG_LOGOUT_SUCCESS = "Logout successful";
    public static final String MSG_TOKEN_REFRESHED = "Token refreshed successfully";
    
    // ===== Error Messages =====
    public static final String ERR_USER_EXISTS = "User already exists";
    public static final String ERR_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERR_TOKEN_INVALID = "Invalid token";
} 