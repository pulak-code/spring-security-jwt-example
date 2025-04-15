package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants;

/**
 * Constants specifically related to security configuration, JWT token handling, roles, and CORS.
 */
public final class SecurityConstants {
    
    private SecurityConstants() {
        // Private constructor to prevent instantiation
    }
    
    // --- JWT Token Properties --- 
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String BEARER = "Bearer "; 
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    public static final String TOKEN_ISSUER = "user-service";
    public static final String JWT_TOKEN = "JWT Token";
    public static final String JWT_SECRET_PROPERTY_NAME = "security.jwt.secret";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 15 * 60 * 1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    // --- JWT Claims --- 
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ISSUED_AT = "iat";
    public static final String CLAIM_EXPIRATION = "exp";
    public static final String CLAIM_ISSUER = "iss";
    public static final String CLAIM_SUBJECT = "sub";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";

    // --- Token Types --- 
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    
    // --- Token Management --- 
    public static final long BLACKLIST_CLEANUP_INTERVAL = 1 * 60 * 60 * 1000;
    public static final String FAILED_TO_CHECK_TOKEN_EXPIRY = "Failed to check token expiry: {}";
    public static final String STORAGE_TYPE_MEMORY = "MEMORY";
    public static final String STORAGE_TYPE_PERSISTENT = "PERSISTENT";
    public static final String STORAGE_TYPE_REDIS = "REDIS";
    public static final String PROP_TOKEN_STORAGE = "security.token.storage";
    
    // --- CORS Settings --- 
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
    public static final long MAX_AGE = 3600L;
    public static final String PROP_CORS_ALLOWED_ORIGINS = "security.cors.allowed-origins";

    // --- Error Messages (Security Related) --- 
    public static final String ERROR_INVALID_TOKEN = "Invalid or expired token";
    public static final String ERROR_MISSING_TOKEN = "Missing authentication token";
    public static final String ERROR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_ACCESS_DENIED = "Access denied";
    public static final String ERROR_USER_DISABLED = "User account is disabled";
    public static final String ERROR_INVALID_ROLE = "Invalid role assignment";
    public static final String ERROR_TOKEN_BLACKLISTED = "Token has been revoked";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Forbidden access";
    
    // --- Success Messages (Security Related) --- 
    public static final String SUCCESS_AUTHENTICATED = "Successfully authenticated";
    public static final String SUCCESS_TOKEN_REFRESHED = "Token successfully refreshed";
    public static final String SUCCESS_LOGOUT = "Successfully logged out";
    
    // --- Role Constants --- 
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    
    // --- Crypto Constants --- 
    public static final String KEY_ALGORITHM = "HmacSHA256";
    public static final String KEYGEN_HASHALGO = "HmacSHA256";
    public static final int MIN_KEY_LENGTH = 256;
    
    // --- URL Patterns / Endpoints --- 
    public static final String URL_ALL_USER = "/user/**";
    public static final String URL_ALL_ADMIN = "/admin/**";
    public static final String AUTH_ENDPOINT = "/api/auth/**";
    public static final String ADMIN_ENDPOINT = "/api/admin/**";
    public static final String USER_ENDPOINT = "/api/user/**";
    public static final String PUBLIC_ENDPOINT = "/api/public/**";
    public static final String SIGN_UP_URL = "/api/auth/user/register";
    public static final String LOGIN_URL = "/api/auth/user/login";
    public static final String AUTH_LOGIN_URL = "/api/auth/user/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/user/refreshtoken";
    public static final String REFRESH_TOKEN_ENDPOINT = AppConstants.AUTH_BASE_PATH + "/refresh-token";
} 