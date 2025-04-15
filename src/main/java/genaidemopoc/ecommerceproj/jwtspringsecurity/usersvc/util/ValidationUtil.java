package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.util;

import java.util.regex.Pattern;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.ValidationConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
   private static final Pattern PASSWORD_PATTERN = Pattern.compile(ValidationConstants.PASSWORD_PATTERN);

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

    public static boolean isValidPassword(String password) {
        if (AppUtil.isNullOrEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidAndStrongPassword(String password) {
        if (AppUtil.isNullOrEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
