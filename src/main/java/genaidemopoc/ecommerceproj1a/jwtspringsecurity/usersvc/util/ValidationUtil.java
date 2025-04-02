package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.util;

import java.util.regex.Pattern;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;

public class ValidationUtil extends AppUtil {
	
private static final Pattern EMAIL_PATTERN = Pattern.compile(UserServiceConstants.EMAIL_REGEX);
   private static final Pattern PASSWORD_PATTERN = Pattern.compile(UserServiceConstants.PASSWORD_REGEX);

	private ValidationUtil() {
		throw new IllegalStateException(UserServiceConstants.UTILITY_CLASS);
	}
	 /**
     * Validates the email format using a regular expression.
     *
     * @param email the email string to validate
     * @return true if the email is valid; false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (AppUtil.isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
	public static boolean isValidAndStrongPassword(String password) {
		if (AppUtil.isNullOrEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
	}
}
