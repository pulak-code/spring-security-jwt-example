package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant;

/**
 * Constants related to input validation throughout the application.
 * Centralizes validation rules, regex patterns, and error messages.
 */
public final class ValidationConstants {
    
    private ValidationConstants() {
        // Private constructor to prevent instantiation
    }
    
    // Regex patterns
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,50}$";
    public static final String NAME_PATTERN = "^[a-zA-Z\\s'-]{2,100}$";
    public static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";
    public static final String URL_PATTERN = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    public static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
    public static final String WHITESPACE_REGEX = "\\s+";
    
    // Size constraints
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
    
    // Validation error messages
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email format is invalid";
    public static final String EMAIL_TOO_LONG = "Email cannot exceed " + MAX_EMAIL_LENGTH + " characters";
    
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters";
    public static final String PASSWORD_TOO_LONG = "Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters";
    public static final String PASSWORD_INVALID = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
    
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_TOO_SHORT = "Name must be at least " + MIN_NAME_LENGTH + " characters";
    public static final String NAME_TOO_LONG = "Name cannot exceed " + MAX_NAME_LENGTH + " characters";
    public static final String NAME_INVALID = "Name can only contain letters, spaces, hyphens, and apostrophes";
    
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_TOO_SHORT = "Username must be at least " + MIN_USERNAME_LENGTH + " characters";
    public static final String USERNAME_TOO_LONG = "Username cannot exceed " + MAX_USERNAME_LENGTH + " characters";
    public static final String USERNAME_INVALID = "Username can only contain letters, numbers, dots, underscores, and hyphens";
    
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String ROLE_INVALID = "Invalid role";
    
    public static final String PHONE_INVALID = "Phone number format is invalid";
    public static final String URL_INVALID = "URL format is invalid";
    public static final String ID_INVALID = "ID format is invalid";
    
    // Date format
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // Validation groups
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String SEARCH = "search";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String PASSWORD_RESET = "passwordReset";
    public static final String PASSWORD_CHANGE = "passwordChange";
} 