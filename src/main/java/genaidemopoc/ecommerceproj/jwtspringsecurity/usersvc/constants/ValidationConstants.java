package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants;

/**
 * Constants related to input validation throughout the application.
 * Centralizes validation rules, regex patterns, size constraints, and error messages.
 */
public final class ValidationConstants {
    
    private ValidationConstants() {
        // Private constructor to prevent instantiation
    }
    
    // --- Regex Patterns ---
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,50}$";
    public static final String NAME_PATTERN = "^[a-zA-Z\\s'-]{2,100}$";
    public static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";
    public static final String URL_PATTERN = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String WHITESPACE_REGEX = "\\s+";

    // --- Size Constraints --- 
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MIN_ROLE_LENGTH = 3;
    public static final int MAX_ROLE_LENGTH = 20;
    public static final int MAX_ADDRESS_LENGTH = 200;
    public static final int MAX_DESCRIPTION_LENGTH = 500;

    // --- Validation Error Messages ---
    // Email
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_EMPTY = "Email cannot be empty";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String EMAIL_INVALID_FORMAT = "Invalid email format %s";
    public static final String EMAIL_TOO_LONG = "Email cannot exceed " + MAX_EMAIL_LENGTH + " characters";

    // Password
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_EMPTY = "Password cannot be empty";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    public static final String PASSWORD_TOO_LONG = "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters";
    public static final String PASSWORD_INVALID = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
    public static final String PASSWORD_FORMAT = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace";
    public static final String PASSWORD_REQUIREMENTS = "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character";

    // Name
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_TOO_SHORT = "Name must be at least " + MIN_NAME_LENGTH + " characters";
    public static final String NAME_TOO_LONG = "Name cannot exceed " + MAX_NAME_LENGTH + " characters";
    public static final String NAME_INVALID = "Name can only contain letters, spaces, hyphens, and apostrophes";
    public static final String NAME_LENGTH = "Name must be between 2 and 100 characters";

    // Username (Note: Username logic was removed previously, but keeping validation constants for potential future use)
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_TOO_SHORT = "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    public static final String USERNAME_TOO_LONG = "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters";
    public static final String USERNAME_INVALID = "Username can only contain letters, numbers, dots, underscores, and hyphens";
    public static final String USERNAME_FORMAT = "Username must be 3-50 characters long and can only contain letters, numbers, underscores, and hyphens";

    // Role
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String ROLE_INVALID = "Invalid role";

    // Other
    public static final String PHONE_INVALID = "Phone number format is invalid";
    public static final String URL_INVALID = "URL format is invalid";
    public static final String ID_INVALID = "ID format is invalid";
    
    // --- Date Format ---
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // --- Validation Groups ---
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String SEARCH = "search";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String PASSWORD_RESET = "passwordReset";
    public static final String PASSWORD_CHANGE = "passwordChange";
} 