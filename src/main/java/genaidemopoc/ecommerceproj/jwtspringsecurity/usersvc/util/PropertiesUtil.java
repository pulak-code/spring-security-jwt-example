package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;

@Component
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class PropertiesUtil {
	private static final Logger propsUtilLogger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String PROPERTIES_FILE_PATH = "classpath:application.yml";
	private static final Properties properties = new Properties();
	
	@Autowired
	private Environment env;

	static {
		loadProperties();
	}

	private static void loadProperties() {
		try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH.replace("classpath:", ""))) {
			if (input == null) {
				propsUtilLogger.error("Sorry, unable to find " + PROPERTIES_FILE_PATH);
				return;
			}
			// For YML files, we don't directly load into Properties object
			// Spring will handle this via the YamlPropertySourceFactory
			propsUtilLogger.info("Properties file loaded successfully from: {}", PROPERTIES_FILE_PATH);
		} catch (IOException ex) {
			propsUtilLogger.error("Error loading properties file: {}", PROPERTIES_FILE_PATH, ex);
		}
	}

	public PropertiesUtil() {
		// Default constructor for Spring
	}
	
	/**
	 * Save a property to the application.properties file
	 * 
	 * @param key Property key
	 * @param value Property value
	 * @param comment Comment for the property
	 */
	public static void saveProperty(String key, String value, String comment) {
		try {
			// First load existing properties
			Properties props = new Properties();
			try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
				props.load(fis);
			} catch (IOException e) {
				propsUtilLogger.warn("Could not load existing properties file: {}", e.getMessage());
			}
			
			// Set or update the property
			props.setProperty(key, value);
			
			// Save properties back to file
			try (FileWriter fw = new FileWriter(PROPERTIES_FILE_PATH)) {
				props.store(fw, comment);
				propsUtilLogger.info("Successfully saved property: {}", key);
			}
		} catch (IOException e) {
			propsUtilLogger.error("Failed to save property: {}", e.getMessage());
		}
	}
	
	/**
	 * Save a secret key (base64 encoded) to the application.properties file
	 * 
	 * @param key Property key
	 * @param value Secret value to encode
	 * @param comment Comment for the property
	 */
	public static void saveSecretKey(String key, String value, String comment) {
		String encodedKey = Base64.getEncoder().encodeToString(value.getBytes());
		saveProperty(key, encodedKey, comment);
	}
	
	/**
	 * Save a JWT token to the application.properties file
	 * 
	 * @param token JWT token
	 */
	public static void saveJwtToken(String token) {
		saveProperty("security.jwt.access.token", token, "JWT Access Token");
	}
	
	/**
	 * Get a string property value
	 * 
	 * @param key Property key
	 * @param defaultValue Default value if not found
	 * @return Property value or default
	 */
	public String getProperty(String key, String defaultValue) {
		return env.getProperty(key, defaultValue);
	}
	
	/**
	 * Get an integer property value
	 * 
	 * @param key Property key
	 * @param defaultValue Default value if not found
	 * @return Property value or default
	 */
	public Integer getIntProperty(String key, Integer defaultValue) {
		String value = env.getProperty(key);
		try {
			return value != null ? Integer.parseInt(value) : defaultValue;
		} catch (NumberFormatException e) {
			propsUtilLogger.warn("Failed to parse integer property {}: {}", key, e.getMessage());
			return defaultValue;
		}
	}
	
	/**
	 * Get a long property value
	 * 
	 * @param key Property key
	 * @param defaultValue Default value if not found
	 * @return Property value or default
	 */
	public Long getLongProperty(String key, Long defaultValue) {
		String value = env.getProperty(key);
		try {
			return value != null ? Long.parseLong(value) : defaultValue;
		} catch (NumberFormatException e) {
			propsUtilLogger.warn("Failed to parse long property {}: {}", key, e.getMessage());
			return defaultValue;
		}
	}
	
	/**
	 * Get a boolean property value
	 * 
	 * @param key Property key
	 * @param defaultValue Default value if not found
	 * @return Property value or default
	 */
	public Boolean getBooleanProperty(String key, Boolean defaultValue) {
		String value = env.getProperty(key);
		return value != null ? Boolean.parseBoolean(value) : defaultValue;
	}
	
	/**
	 * Get the stored JWT token
	 * 
	 * @return JWT token or null if not found
	 */
	public String getJwtToken() {
		return env.getProperty("security.jwt.access.token");
	}
}
