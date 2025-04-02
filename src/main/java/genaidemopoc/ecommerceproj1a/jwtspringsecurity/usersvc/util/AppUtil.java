package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.util;

public class AppUtil {
	    /**
	     * Checks if the provided string is null or empty after trimming.
	     *
	     * @param str the string to check
	     * @return true if the string is null or empty; false otherwise
	     */
	    public static boolean isNullOrEmpty(String str) {
	        return str == null || str.trim().isEmpty();
	    }

	    /**
	     * Sanitizes a string by trimming it and replacing multiple spaces with a single space.
	     *
	     * @param input the input string
	     * @return the sanitized string, or null if input is null
	     */
	    public static String sanitizeString(String input) {
	        if (input == null) {
	            return null;
	        }
	        return input.trim().replaceAll("\\s+", " ");
	    }
	    
	}
