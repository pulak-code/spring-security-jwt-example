package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Validates security properties and logs warnings for potentially insecure configurations.
 */
@Component
@RequiredArgsConstructor
public class SecurityPropertiesValidator {
    private static final Logger logger = LoggerFactory.getLogger(SecurityPropertiesValidator.class);
    
    private final SecurityProperties securityProperties;

    @EventListener(ApplicationStartedEvent.class)
    public void validateSecurityProperties() {
        validateCorsConfiguration();
        validateAuthConfiguration();
        validateHeadersConfiguration();
        validateLockoutConfiguration();
    }

    private void validateCorsConfiguration() {
        SecurityProperties.Cors cors = securityProperties.getCors();

        // Check for wildcard origins
        if (cors.getAllowedOrigins().contains("*")) {
            logger.warn("Security risk: Wildcard CORS origin (*) is configured. " +
                       "This is not recommended for production.");
        }

        // Check for secure origins
        cors.getAllowedOrigins().stream()
            .filter(origin -> origin.startsWith("http://"))
            .forEach(origin -> logger.warn("Security risk: Non-HTTPS origin configured: {}", origin));
    }

    private void validateAuthConfiguration() {
        SecurityProperties.Auth auth = securityProperties.getAuth();

        // Check token expiration
        if (auth.getTokenExpiration().toHours() > 24) {
            logger.warn("Security risk: JWT token expiration > 24 hours");
        }

        // Check refresh token expiration
        if (auth.getRefreshTokenExpiration().toDays() > 7) {
            logger.warn("Security risk: Refresh token expiration > 7 days");
        }

        // Check HTTPS requirement
        if (!auth.getRequireHttps()) {
            logger.warn("Security risk: HTTPS is not required for authentication endpoints");
        }
    }

    private void validateHeadersConfiguration() {
        SecurityProperties.Headers headers = securityProperties.getHeaders();

        // Check HSTS configuration
        if (!headers.getEnableHsts()) {
            logger.warn("Security risk: HSTS is disabled");
        }

        // Check frame options
        if (!headers.getDenyFrameOptions()) {
            logger.warn("Security risk: Frame options deny is disabled");
        }

        // Check XSS protection
        if (!headers.getEnableXssProtection()) {
            logger.warn("Security risk: XSS protection is disabled");
        }
    }

    private void validateLockoutConfiguration() {
        SecurityProperties.Lockout lockout = securityProperties.getLockout();

        // Check max attempts
        if (lockout.getMaxAttempts() > 5) {
            logger.warn("Security risk: High number of allowed login attempts ({})",
                lockout.getMaxAttempts());
        }

        // Check lockout duration
        if (lockout.getDuration().toMinutes() < 15) {
            logger.warn("Security risk: Short lockout duration ({} minutes)",
                lockout.getDuration().toMinutes());
        }

        // Check progressive delay
        if (!lockout.getEnableProgressiveDelay()) {
            logger.warn("Security recommendation: Progressive delay is disabled");
        }
    }
}

