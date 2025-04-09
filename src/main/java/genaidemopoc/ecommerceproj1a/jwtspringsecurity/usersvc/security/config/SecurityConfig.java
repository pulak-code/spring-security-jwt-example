package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import jakarta.annotation.PostConstruct;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.authprovider.CustomAuthenticationProvider;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter.SecurityCorsFilter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter.JWTAuthenticationFilter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the application.
 * Handles authentication, authorization, and security headers.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomUserDetailsService userDetailsService;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationProvider authenticationProvider;
    private final SecurityCorsFilter securityCorsFilter;
    private final MeterRegistry meterRegistry;

    private Counter authSuccessCounter;
    private Counter authFailureCounter;

    @PostConstruct
    public void initMetrics() {
        authSuccessCounter = Counter.builder("security.authentication")
            .description("Number of successful authentication attempts")
            .tag("result", "success")
            .register(meterRegistry);

        authFailureCounter = Counter.builder("security.authentication")
            .description("Number of failed authentication attempts")
            .tag("result", "failure")
            .register(meterRegistry);
        
        logger.info("Security metrics initialized");
    }

    /**
     * Configures the main security filter chain with proper ordering of security features:
     * 1. CORS/CSRF configuration
     * 2. Security headers
     * 3. Session management
     * 4. Authorization rules
     * 5. Authentication provider and filters
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // 1. CORS/CSRF Configuration
            .cors().disable()  // CORS handled by SecurityCorsFilter
            .csrf().disable()  // Disabled for stateless API
            
            // 2. Security Headers Configuration
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())  // Prevent clickjacking
                .xssProtection(xss -> xss.enable())   // Enable XSS protection
                .contentSecurityPolicy(csp ->         // Strict CSP
                    csp.policyDirectives("default-src 'self'; frame-ancestors 'none';"))
                .httpStrictTransportSecurity(hsts ->  // Force HTTPS
                    hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                .referrerPolicy(referrer -> referrer.strictOrigin())
                .permissionsPolicy(permissions -> permissions.disable())
            )
            
            // 3. Session Management
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 4. Authorization Rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml"
                ).permitAll()
                // Authentication endpoints
                .requestMatchers(
                    "/api/auth/user/register",
                    "/api/auth/admin/register",
                    "/api/auth/user/login",
                    "/api/auth/admin/login"
                ).permitAll()
                // Admin endpoints protection
                .requestMatchers(SecurityConstants.URL_ALL_ADMIN).hasRole("ADMIN")
                // User endpoints protection
                .requestMatchers(SecurityConstants.URL_ALL_USER).hasRole("USER")
                // Health check endpoint
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll()
                // Protect all other actuator endpoints
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                // Require authentication for all other endpoints
                .anyRequest().authenticated()
            )
            // 5. Authentication Provider and Filters
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(
                securityCorsFilter, 
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(
                jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        logger.info("Initializing AuthenticationManager with CustomAuthenticationProvider");
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Increased strength from default 10
    }

    /**
     * Increment success counter for metrics
     */
    public void recordAuthenticationSuccess() {
        authSuccessCounter.increment();
        logger.debug("Authentication success recorded");
    }

    /**
     * Increment failure counter for metrics
     */
    public void recordAuthenticationFailure() {
        authFailureCounter.increment();
        logger.debug("Authentication failure recorded");
    }
}
