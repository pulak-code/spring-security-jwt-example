package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Configuration properties for security settings.
 * These can be configured in application.yml/properties.
 */
@Data
@ConfigurationProperties(prefix = "security")
@ConfigurationPropertiesScan
@Validated
@Component
public class SecurityProperties {

    @Valid
    private final Cors cors = new Cors();
    
    @Valid
    private final Auth auth = new Auth();
    
    @Valid
    private final Headers headers = new Headers();
    
    @Valid
    private final Lockout lockout = new Lockout();

    @Data
    public static class Auth {
        /**
         * JWT issuer identifier.
         */
        @NotEmpty(message = "Issuer cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Issuer must be alphanumeric")
        private String issuer = "userservice1b";

        /**
         * JWT token expiration duration.
         */
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Duration tokenExpiration = Duration.ofHours(24);

        /**
         * JWT refresh token expiration duration.
         */
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Duration refreshTokenExpiration = Duration.ofDays(7);

        /**
         * Secret key for JWT signing.
         * Must be at least 32 characters in production.
         */
        @NotEmpty(message = "Secret key cannot be empty")
        @Size(min = 32, message = "Secret key must be at least 32 characters long")
        private String secretKey;

        /**
         * Whether to require HTTPS for authentication endpoints.
         */
        @NotNull
        private Boolean requireHttps = true;
    }

    @Data
    public static class Cors {
        /**
         * List of allowed origins for CORS requests.
         * Each origin must be a valid URL starting with http:// or https://
         * Wildcards (*) are not allowed for security reasons and must be explicitly reviewed.
         */
        @NotEmpty(message = "At least one allowed origin must be specified")
        private List<@NotNull @Pattern(regexp = "^https?://[\\w.-]+(:\\d+)?$", message = "Origins must be valid URLs with http:// or https:// and no wildcards") String> allowedOrigins = List.of(
            "http://localhost:4200"
        );

        /**
         * List of allowed HTTP methods for CORS requests.
         */
        @NotEmpty(message = "At least one allowed method must be specified")
        private List<@NotNull @Pattern(regexp = "^(GET|POST|PUT|DELETE|PATCH|OPTIONS|HEAD)$", message = "Method must be a valid HTTP method") String> allowedMethods = List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        );

        /**
         * List of allowed headers for CORS requests.
         */
        @NotEmpty(message = "At least one allowed header must be specified")
        private List<@NotNull @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Headers must contain only alphanumeric characters, hyphens, and underscores") String> allowedHeaders = List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        );

        /**
         * List of headers exposed to the client.
         */
        @NotNull
        private List<@NotNull @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Exposed headers must contain only alphanumeric characters, hyphens, and underscores") String> exposedHeaders = List.of(
            "Authorization",
            "Content-Disposition"
        );

        /**
         * Max age of CORS preflight requests cache in seconds.
         */
        @NotNull
        @Min(value = 0, message = "Max age cannot be negative")
        @Max(value = 86400, message = "Max age cannot exceed 24 hours")
        private Long maxAge = 3600L;

        /**
         * Whether to allow credentials in CORS requests.
         */
        @NotNull
        private Boolean allowCredentials = true;
    }

    /**
     * Security headers configuration.
     * Controls HTTP security headers like HSTS, CSP, XSS protection, etc.
     */
    @Data
    public static class Headers {
        /**
         * Whether to enable HTTP Strict Transport Security (HSTS).
         */
        @NotNull(message = "HSTS enablement must be specified")
        private Boolean enableHsts = true;
        
        /**
         * Max age for HSTS headers in seconds.
         * Default is 1 year (31536000 seconds).
         */
        @NotNull(message = "HSTS max age must be specified")
        @Min(value = 0, message = "HSTS max age cannot be negative")
        @Max(value = 63072000, message = "HSTS max age cannot exceed 2 years")
        private Long hstsMaxAge = 31536000L;
        
        /**
         * Whether to include subdomains in HSTS policy.
         */
        @NotNull(message = "HSTS includeSubDomains must be specified")
        private Boolean hstsIncludeSubDomains = true;
        
        /**
         * Content Security Policy header value.
         * Default is restrictive, allowing only same-origin resources.
         */
        @NotNull(message = "Content Security Policy must be specified")
        @Pattern(regexp = "^[^*]*$", message = "Content Security Policy should not contain wildcards (*)")
        private String contentSecurityPolicy = "default-src 'self'; frame-ancestors 'none';";
        
        /**
         * Referrer Policy header value.
         */
        @NotNull(message = "Referrer Policy must be specified")
        @Pattern(regexp = "^(no-referrer|no-referrer-when-downgrade|origin|origin-when-cross-origin|same-origin|strict-origin|strict-origin-when-cross-origin|unsafe-url)$", 
                message = "Invalid Referrer Policy value")
        private String referrerPolicy = "strict-origin";
        
        /**
         * Whether to enable X-XSS-Protection header.
         */
        @NotNull(message = "XSS protection enablement must be specified")
        private Boolean enableXssProtection = true;
        
        /**
         * Whether to deny framing of the application (X-Frame-Options: DENY).
         */
        @NotNull(message = "Frame options must be specified")
        private Boolean denyFrameOptions = true;
    }

    /**
     * Account lockout configuration.
     * Controls behavior for locking accounts after failed authentication attempts.
     */
    @Data
    public static class Lockout {
        /**
         * Maximum number of failed authentication attempts before account lockout.
         */
        @NotNull(message = "Max attempts must be specified")
        @Min(value = 3, message = "Max attempts must be at least 3")
        @Max(value = 10, message = "Max attempts cannot exceed 10")
        private Integer maxAttempts = 5;
        
        /**
         * Duration for which an account remains locked after exceeding max attempts.
         * Duration for which an account remains locked after exceeding max attempts.
         */
        @NotNull(message = "Lockout duration must be specified")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Duration duration = Duration.ofMinutes(15);

        /**
         * Validates that lockout duration is within a reasonable range (1 minute to 24 hours).
         */
        @AssertTrue(message = "Lockout duration must be between 1 minute and 24 hours")
        private boolean isLockoutDurationValid() {
            return duration != null && 
                   !duration.isNegative() && 
                   !duration.isZero() && 
                   duration.compareTo(Duration.ofMinutes(1)) >= 0 && 
                   duration.compareTo(Duration.ofHours(24)) <= 0;
        }
        /**
         * Whether to enable progressive delay between authentication attempts.
         */
        @NotNull(message = "Progressive delay enablement must be specified")
        private Boolean enableProgressiveDelay = true;
        
        /**
         * Initial delay after first failed authentication attempt.
         */
        @NotNull(message = "Initial delay must be specified")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Duration initialDelay = Duration.ofSeconds(1);

        /**
         * Validates that initial delay is within a reasonable range (1 second to 1 minute).
         */
        @AssertTrue(message = "Initial delay must be between 1 second and 1 minute")
        private boolean isInitialDelayValid() {
            return initialDelay != null && 
                   !initialDelay.isNegative() && 
                   !initialDelay.isZero() && 
                   initialDelay.compareTo(Duration.ofSeconds(1)) >= 0 && 
                   initialDelay.compareTo(Duration.ofMinutes(1)) <= 0;
        }
        
        /**
         * Maximum delay between authentication attempts.
         */
        @NotNull(message = "Max delay must be specified")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private Duration maxDelay = Duration.ofMinutes(5);

        /**
         * Validates that max delay is within a reasonable range (initialDelay to 30 minutes).
         */
        @AssertTrue(message = "Max delay must be between initial delay and 30 minutes")
        private boolean isMaxDelayValid() {
            return maxDelay != null && 
                   initialDelay != null &&
                   !maxDelay.isNegative() && 
                   !maxDelay.isZero() && 
                   maxDelay.compareTo(initialDelay) >= 0 && 
                   maxDelay.compareTo(Duration.ofMinutes(30)) <= 0;
        }
    }
}

