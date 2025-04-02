package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.constants;

/**
 * Constants for log messages used in integration tests.
 * Centralizes all log messages to ensure consistency and easier maintenance.
 */
public final class LogMessageConstants {
    
    private LogMessageConstants() {
        // Private constructor to prevent instantiation
    }
    
    // Test skip messages
    public static final String SKIPPING_TEST = "Skipping test - prerequisite not available";
    public static final String TOKEN_MISSING = "Test skipped - token not available";
    
    // Error messages
    public static final String TEST_FAILED = "Test failed: ";
    public static final String UNAUTHORIZED_ERROR = "Unexpected authorization error";
    
    // Validation messages
    public static final String USER_REGISTRATION_SHOULD_SUCCEED = "User registration should succeed";
    public static final String USER_LOGIN_SHOULD_SUCCEED = "User login should succeed";
    public static final String ACCESS_CONTROL_SHOULD_WORK = "Access control should properly restrict non-admin users";
    public static final String ADMIN_ACCESS_SHOULD_SUCCEED = "Admin should be able to access protected endpoints";
} 