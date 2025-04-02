package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.util;

public final class SearchUtil {
    private SearchUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sanitizes a search parameter by handling null and empty values.
     * @param param The search parameter to sanitize
     * @return A sanitized string that can be used in regex patterns
     */
    public static String sanitizeSearchParam(String param) {
        if (param == null || param.trim().isEmpty()) {
            return "";
        }
        return param.trim();
    }

    /**
     * Creates a case-insensitive regex pattern for MongoDB queries.
     * @param param The search parameter to create a pattern for
     * @return A regex pattern string that can be used in MongoDB queries
     */
    public static String createCaseInsensitivePattern(String param) {
        String sanitized = sanitizeSearchParam(param);
        if (sanitized.isEmpty()) {
            return "";
        }
        return "^" + sanitized + "$";
    }

    /**
     * Creates a case-insensitive contains pattern for MongoDB queries.
     * @param param The search parameter to create a pattern for
     * @return A regex pattern string that can be used in MongoDB queries
     */
    public static String createContainsPattern(String param) {
        String sanitized = sanitizeSearchParam(param);
        if (sanitized.isEmpty()) {
            return "";
        }
        return sanitized;
    }
} 