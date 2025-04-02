package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants specifically related to security configuration and JWT token handling.
 * Centralizes all security-related constants to ensure consistency and easier maintenance.
 */
public final class SecurityConstants {
    
    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
    
    // JWT Token properties
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String BEARER = "Bearer ";
    public static final String BEARER1 = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORIZATION = "Authorization";
    public static final String SIGN_UP_URL = "/api/auth/user/register";
    public static final String LOGIN_URL = "/api/auth/user/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/user/refreshtoken";
    public static final String TOKEN_ISSUER = "user-service";
    public static final String JWT_TOKEN = "JWT Token";
    public static final String JWT_SECRET = "security.jwt.secret";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    
    // JWT Claims
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ISSUED_AT = "iat";
    public static final String CLAIM_EXPIRATION = "exp";
    public static final String CLAIM_ISSUER = "iss";
    public static final String CLAIM_SUBJECT = "sub";
    
    // Token types
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";
    
    // Token expiration times in milliseconds
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 days
    public static final long BLACKLIST_CLEANUP_INTERVAL = 1 * 60 * 60 * 1000; // 1 hour
    public static final String FAILED_TO_CHECK_TOKEN_EXPIRY = "Failed to check token expiry: {}";
    
    // CORS settings
    public static final String ALLOWED_ORIGIN_ALL = "*";
    public static final String ALLOWED_ORIGIN_LOCAL = "http://localhost:3000";
    public static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String ALLOWED_HEADERS = "Origin, Content-Type, Accept, Authorization, X-Requested-With, Refresh-Token";
    public static final String EXPOSED_HEADERS = "Authorization, Refresh-Token";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String OPTIONS = "OPTIONS";
    public static final String HTTP_HEADERS = "Authorization, Content-Type, X-Requested-With";
    public static final String HTTP_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final long MAX_AGE = 3600L;
    
    // Error messages
    public static final String ERROR_INVALID_TOKEN = "Invalid or expired token";
    public static final String ERROR_MISSING_TOKEN = "Missing authentication token";
    public static final String ERROR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_ACCESS_DENIED = "Access denied";
    public static final String ERROR_USER_DISABLED = "User account is disabled";
    public static final String ERROR_INVALID_ROLE = "Invalid role assignment";
    public static final String ERROR_TOKEN_BLACKLISTED = "Token has been revoked";
    
    // Success messages
    public static final String SUCCESS_AUTHENTICATED = "Successfully authenticated";
    public static final String SUCCESS_TOKEN_REFRESHED = "Token successfully refreshed";
    public static final String SUCCESS_LOGOUT = "Successfully logged out";
    
    // Role constants
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    
    // Crypto constants
    public static final String KEY_ALGORITHM = "HmacSHA256";
    public static final String KEYGEN_HASHALGO = "HmacSHA256";
    public static final int MIN_KEY_LENGTH = 256;
    
    // Token storage options
    public static final String STORAGE_TYPE_MEMORY = "MEMORY";
    public static final String STORAGE_TYPE_PERSISTENT = "PERSISTENT";
    public static final String STORAGE_TYPE_REDIS = "REDIS";
    
    // Security configuration properties
    public static final String PROP_JWT_SECRET = "security.jwt.secret";
    public static final String PROP_TOKEN_STORAGE = "security.token.storage";
    public static final String PROP_CORS_ALLOWED_ORIGINS = "security.cors.allowed-origins";
    
    // URL Patterns
    public static final String URL_ALL_USER = "/user/**";
    public static final String URL_ALL_ADMIN = "/admin/**";
    public static final String URL_ALL = "/api/auth/**";
} 