package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Collections;
import java.time.Duration;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config.SecurityConfig;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config.SecurityProperties;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.authprovider.CustomAuthenticationProvider;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.metrics.AuthenticationMetricsService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.FailedLoginAttemptRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.UserService;

/**
 * Base test configuration providing common beans for tests
 */
@TestConfiguration
@Import({SecurityConfig.class})
public class BaseTestConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refresh-secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    /**
     * Provides a test-specific password encoder with lower strength for faster tests
     */
    @Bean
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return mock(MeterRegistry.class);
    }

    @Bean
    public AuthenticationMetricsService authenticationMetricsService(MeterRegistry meterRegistry) {
        return new AuthenticationMetricsService(meterRegistry);
    }

    @Bean
    public FailedLoginAttemptRepository failedLoginAttemptRepository() {
        return mock(FailedLoginAttemptRepository.class);
    }

    /**
     * Provides test-specific security properties
     */
    @Bean
    public SecurityProperties securityProperties() {
        SecurityProperties properties = new SecurityProperties();
        SecurityProperties.Auth auth = properties.getAuth();
        auth.setSecretKey(jwtSecret);
        auth.setRefreshSecretKey(jwtRefreshSecret);
        auth.setTokenExpiration(Duration.ofMillis(jwtExpiration));
        auth.setRefreshTokenExpiration(Duration.ofMillis(jwtRefreshExpiration));
        return properties;
    }

    /**
     * Provides a test-specific JWT util
     */
    @Bean
    public JWTUtil jwtUtil(UserService userService, SecurityProperties securityProperties) {
        return new JWTUtil(userService, securityProperties);
    }

    /**
     * Provides a test-specific user details service
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * Provides a test-specific authentication provider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(
            CustomUserDetailsService userDetailsService,
            @Lazy PasswordEncoder passwordEncoder,
            AuthenticationMetricsService metricsService,
            FailedLoginAttemptRepository failedLoginAttemptRepository) {
        return new CustomAuthenticationProvider(
                userDetailsService,
                passwordEncoder,
                metricsService,
                failedLoginAttemptRepository);
    }

    /**
     * Provides a test-specific authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            CustomAuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

}
