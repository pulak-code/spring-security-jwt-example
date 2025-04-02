package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

public final class UserServiceTestUserServiceConstants {
    private UserServiceTestUserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // Test User Data
    public static final String TEST_USER_EMAIL = "test@example.com";
    public static final String TEST_USER_PASSWORD = "Test@123";
    public static final String TEST_USER_NAME = "Test User";
    public static final String TEST_ADMIN_EMAIL = "admin@example.com";
    public static final String TEST_ADMIN_PASSWORD = "Admin@123";
    public static final String TEST_ADMIN_NAME = "Admin User";

    // Test Token Data
    public static final String TEST_TOKEN = "test.jwt.token";
    public static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    public static final String TEST_INVALID_TOKEN = "invalid.token";
    public static final String TEST_EXPIRED_TOKEN = "expired.token";

    // Test Role Data
    public static final String TEST_USER_ROLE = "ROLE_USER";
    public static final String TEST_ADMIN_ROLE = "ROLE_ADMIN";

    // Test Endpoints
    public static final String TEST_AUTH_ENDPOINT = "/api/auth/test";
    public static final String TEST_USER_ENDPOINT = "/api/user/test";
    public static final String TEST_ADMIN_ENDPOINT = "/api/admin/test";

    // Test Messages
    public static final String TEST_SUCCESS_MESSAGE = "Test operation successful";
    public static final String TEST_ERROR_MESSAGE = "Test operation failed";
    public static final String TEST_VALIDATION_MESSAGE = "Test validation message";
} 