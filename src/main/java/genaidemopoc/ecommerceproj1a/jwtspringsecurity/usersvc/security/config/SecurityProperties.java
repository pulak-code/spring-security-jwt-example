package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.validation.annotation.Validated;

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
tern(regexp = "^https?://.*", message = "Origins must start with http:// or https://")
        })
        private List<String> allowedOrigins = List.of(
            "http://localhost:3000",
            "http://localhost:4200"
        );

        /**
         * List of allowed HTTP methods for CORS requests.
         */
        @NotEmpty(message = "At least one allowed method must be specified")
        @Pattern.List({
            @Pattern(regexp = "^(GET|POST|PUT|DELETE|OPTIONS)$", message = "Invalid HTTP method")
        })
        private List<String> allowedMethods = List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        );

        /**
         * List of allowed headers for CORS requests.
         */
        @NotEmpty(message = "At least one allowed header must be specified")
        private List<String> allowedHeaders = List.of(
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
        private List<String> exposedHeaders = List.of(
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

    @Data
    public static class Auth {
        private String issuer = "userservice1b";
        private Duration tokenExpiration = Duration.ofHours(24);
        private Duration refreshTokenExpiration = Duration.ofDays(7);
        private String secretKey;
        private Boolean requireHttps = true;
    }

    @Data
    public static class Headers {
        private Boolean enableHsts = true;
        private Long hstsMaxAge = 31536000L;
        private Boolean hstsIncludeSubDomains = true;
        private String contentSecurityPolicy = "default-src 'self'; frame-ancestors 'none';";
        private String referrerPolicy = "strict-origin";
        private Boolean enableXssProtection = true;
        private Boolean denyFrameOptions = true;
    }

    @Data
    public static class Lockout {
        private Integer maxAttempts = 5;
        private Duration duration = Duration.ofMinutes(15);
        private Boolean enableProgressiveDelay = true;
        private Duration initialDelay = Duration.ofSeconds(1);
        private Duration maxDelay = Duration.ofMinutes(5);
    }
}

