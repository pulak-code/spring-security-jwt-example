package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants used throughout the user service.
 */
public final class UserServiceConstants {
    private UserServiceConstants() {
        // Private constructor to prevent instantiation
    }

    // Date format constants
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // User-related constants
    public static final String USER_ALREADY_EXISTS_WITH_EMAIL = "User already exists with email: %s";
    public static final String WEAK_PASSWORD_PROVIDED_WITH_PASSWORD = "Weak password provided: %s";
    public static final String WEAK_PASSWORD_PROVIDED = "Weak password provided";
    public static final String INVALID_EMAIL = "Invalid email address";

    // JWT-related constants
    public static final String KEYGEN_HASHALGO = "HmacSHA256";
    public static final String ERROR_GENERATING_KEY = "Error generating key: %s";

    // Role constants
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_USER_WITH_PREFIX = ROLE_PREFIX + ROLE_USER;
    public static final String ROLE_ADMIN_WITH_PREFIX = ROLE_PREFIX + ROLE_ADMIN;

    // API endpoint constants
    public static final String API_AUTH = "/api/auth";
    public static final String API_USER = "/api/user";
    public static final String API_ADMIN = "/api/admin";

    // Error message constants
    public static final String ERROR_USER_NOT_FOUND = "User not found with email: %s";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid credentials";
    public static final String ERROR_TOKEN_EXPIRED = "Token has expired";
    public static final String ERROR_TOKEN_INVALID = "Invalid token";
    public static final String ERROR_TOKEN_BLACKLISTED = "Token has been blacklisted";
    public static final String ERROR_REFRESH_TOKEN_INVALID = "Invalid refresh token";
    public static final String ERROR_REFRESH_TOKEN_EXPIRED = "Refresh token has expired";
    public static final String ERROR_REFRESH_TOKEN_BLACKLISTED = "Refresh token has been blacklisted";
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: %s";
    public static final String INVALID_CREDENTIALS_WITH_EMAIL = "Invalid credentials for email: %s";

    // Success message constants
    public static final String SUCCESS_USER_CREATED = "User created successfully";
    public static final String SUCCESS_USER_UPDATED = "User updated successfully";
    public static final String SUCCESS_USER_DELETED = "User deleted successfully";
    public static final String SUCCESS_LOGOUT = "Logged out successfully";
    public static final String SUCCESS_TOKEN_REFRESHED = "Token refreshed successfully";

    // Validation constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PASSWORD_POLICY = "Password must contain at least 8 characters, including uppercase, lowercase, numbers and special characters";

    // Database constraints
    public static final int FIELD_LENGTH_NAME = 100;
    public static final int FIELD_LENGTH_PHONE = 20;

    // Validation messages
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String NAME_IS_REQUIRED = "Name is required";

    // URL & Endpoints
    public static final String API_USERS_BASE_PATH = "/api/users";
    public static final String API_AUTH_BASE_PATH = "/api/auth";
    public static final String API_ADMIN_BASE_PATH = "/api/admin";
    public static final String API_USERS_URL_ENDPOINT = "/api/users";
    public static final String API_AUTH_URL_ENDPOINT = "/api/auth";
    
    public static final String USER_REGISTER_ENDPOINT = "/user/register";
    public static final String USER_LOGIN_ENDPOINT = "/user/login";
    public static final String USER_REFRESH_TOKEN_ENDPOINT = "/user/refreshtoken";
    public static final String USER_LOGOUT_ENDPOINT = "/user/logout";
    public static final String USER_LOGOUT_ALL_ENDPOINT = "/user/logout-all";
    public static final String USER_CURRENT_ENDPOINT = "/user/current";
    public static final String USER_PROFILE_ENDPOINT = "/user/profile";
    public static final String USER_UPDATE_ENDPOINT = "/user/update";
    public static final String USER_DELETE_ENDPOINT = "/user/delete";
    public static final String USER_ENDPOINT = "/user";
    public static final String LOGOUT_ENDPOINT = "/logout";
    
    public static final String ADMIN_USERS_ENDPOINT = "/user";
    public static final String ADMIN_USERS_SEARCH_ENDPOINT = "/user/search";
    public static final String ADMIN_USER_BY_ID_ENDPOINT = "/user/{id}";
    public static final String ADMIN_USER_UPDATE_ENDPOINT = "/user/{id}/update";
    public static final String ADMIN_USER_DELETE_ENDPOINT = "/user/{id}/delete";
    
    // Security URL Patterns
    public static final String URL_PATTERN_API_USERS = "/api/users/**";
    public static final String URL_PATTERN_API_AUTH = "/api/auth/**";
    public static final String URL_PATTERN_API_ADMIN = "/api/admin/**";
    public static final String URL_PATTERN_PUBLIC = "/public/**";
    public static final String URL_PATTERN_SWAGGER = "/swagger-ui/**";
    public static final String URL_PATTERN_API_DOCS = "/v3/api-docs/**";
    
    // HTTP Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String BEARER_PREFIX = "Bearer ";
    
    // Cookie names
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";
    public static final String ACCESS_TOKEN_COOKIE = "accessToken";
    
    // Response keys
    public static final String MESSAGE_KEY = "message";
    public static final String SUCCESS_KEY = "success";
    public static final String ERROR_KEY = "error";
    public static final String DATA_KEY = "data";
    public static final String TOKEN_KEY = "token";
    public static final String REFRESH_TOKEN_KEY = "refreshToken";
    
    // JWT Constants
    public static final String JWT_ISSUER = "user-service";
    public static final String JWT_AUDIENCE = "web-client";
    public static final long JWT_ACCESS_TOKEN_EXPIRY_MS = 15 * 60 * 1000; // 15 minutes
    public static final long JWT_REFRESH_TOKEN_EXPIRY_MS = 7 * 24 * 60 * 60 * 1000; // 7 days
    public static final String JWT_CLAIM_ROLES = "roles";
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_CLAIM_EMAIL = "email";
    
    // Token Storage Strategies
    public static final String TOKEN_STORAGE_MEMORY = "MEMORY";
    public static final String TOKEN_STORAGE_ENVIRONMENT = "ENVIRONMENT";
    public static final String TOKEN_STORAGE_PROPERTIES = "PROPERTIES";
    public static final String TOKEN_STORAGE_STRATEGY_PROPERTY = "token.storage.strategy";
    
    // Environment Variable Names
    public static final String JWT_ACCESS_TOKEN_ENV = "JWT_ACCESS_TOKEN";
    public static final String JWT_REFRESH_TOKEN_ENV = "JWT_REFRESH_TOKEN";
    
    // Property Keys
    public static final String JWT_SECRET_PROPERTY = "security.jwt.secret";
    public static final String JWT_ACCESS_TOKEN_PROPERTY = "security.jwt.access.token";
    public static final String JWT_REFRESH_TOKEN_PROPERTY = "security.jwt.refresh.token";
    public static final String CORS_ALLOWED_ORIGINS_PROPERTY = "security.cors.allowed-origins";
    
    // Miscellaneous
    public static final String PROPERTIES_FILE_PATH = "src/main/resources/application.properties";
    public static final String FILE_LOCATION = "src/main/resources/application.properties";
    public static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String APPLICATION_JSON = "application/json";
    public static final String UTILITY_CLASS = "Utility class";

    // CORS Headers
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String AUTHORIZATION = "Authorization";
    public static final String HTTP_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String HTTP_HEADERS = "Content-Type, Authorization, X-Requested-With, accept, Origin, Access-Control-Request-Method, Access-Control-Request-Headers";
    public static final String OPTIONS = "OPTIONS";

    // API endpoints
    public static final String URL_ALL = "/api/auth/**";
    public static final String URL_ALL_USER = "/user/**";
    public static final String URL_ALL_ADMIN = "/admin/**";
    
    // HTTP response messages
    public static final String SUCCESS = "Success";
    public static final String ERROR = "Error";
    public static final String FAILURE = "Failure";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String WEAK_PASSWORD = "Password does not meet security requirements";
    public static final String SERVICE_ERROR = "An error occurred in the service";
    
    // Date formats
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_TIMEZONE = "UTC";
    
    // Swagger API documentation
    public static final String API_TITLE = "User Service API";
    public static final String API_DESCRIPTION = "REST API for user management";
    public static final String API_VERSION = "1.0";
    public static final String API_TERMS_URL = "Terms of service";
    public static final String API_LICENSE = "License of API";
    public static final String API_LICENSE_URL = "API license URL";
    
    // Default pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    
    // Configuration properties
    public static final String APP_NAME = "user-service";
    
    // Security Constants
    public static final String BEARER_AUTHENTICATION = "bearerAuth";
    public static final String BEARER1 = "bearer";
    public static final String JWT = "JWT";

    // Operation descriptions
    public static final String REGISTER_USER = "Register User";
    public static final String REGISTERS_A_NEW_USER = "Registers a new user in the system";
    public static final String USER_LOGIN = "User Login";
    public static final String AUTHENTICATES_A_USER_AND_RETURNS_JWT_TOKEN = "Authenticates a user and returns JWT token";
    public static final String GET_USER_BY_ID = "Get User by ID";
    public static final String FETCH_A_USER_USING_THEIR_ID = "Fetch a user using their ID";
} 