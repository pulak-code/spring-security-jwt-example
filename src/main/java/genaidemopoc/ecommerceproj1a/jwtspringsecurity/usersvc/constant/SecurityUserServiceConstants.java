package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

public final class SecurityUserServiceConstants {
    private SecurityUserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // JWT Constants
    public static final String JWT_SECRET = "your-secret-key-here";
    public static final long JWT_EXPIRATION = 86400000; // 24 hours
    public static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 days
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_ROLES = "roles";

    // Security Headers
    public static final String CORS_ALLOW_ORIGIN = "*";
    public static final String CORS_ALLOW_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String CORS_ALLOW_HEADERS = "Authorization, Content-Type";
    public static final String CORS_MAX_AGE = "3600";

    // Security Messages
    public static final String TOKEN_EXPIRED = "Token has expired";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String TOKEN_BLACKLISTED = "Token has been blacklisted";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Forbidden access";

    // Security Roles
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";

    // Security Endpoints
    public static final String AUTH_ENDPOINT = "/api/auth/**";
    public static final String ADMIN_ENDPOINT = "/api/admin/**";
    public static final String USER_ENDPOINT = "/api/user/**";
    public static final String PUBLIC_ENDPOINT = "/api/public/**";
} 