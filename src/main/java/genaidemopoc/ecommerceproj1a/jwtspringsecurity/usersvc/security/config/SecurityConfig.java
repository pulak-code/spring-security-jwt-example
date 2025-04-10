package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config;

// Spring Framework
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import java.util.List;

// Project Specific
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.authprovider.CustomAuthenticationProvider;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter.JWTAuthenticationFilter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
// import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.entrypoint.JwtAuthenticationEntryPoint; // Commented out - Class not found

// Monitoring & Metrics
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

// Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Other
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Security configuration for the application.
 * Handles authentication, authorization, and security headers.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomUserDetailsService userDetailsService;
    // private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // Commented out
    private final JWTAuthenticationFilter jwtAuthenticationFilter; // Corrected field type name
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationProvider authenticationProvider;
    private final MeterRegistry meterRegistry;

    private Counter authSuccessCounter;
    private Counter authFailureCounter;

    // Define Auth Whitelist URLs here for now
    private static final String[] AUTH_WHITELIST_URLS = {
        "/api/auth/**",
        "/users/api/auth/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/actuator/health" 
    };

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
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Uses correct type
            // .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Commented out
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST_URLS).permitAll() // Use local definition
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Uses correct type

        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager() {
        logger.info("Initializing AuthenticationManager with CustomAuthenticationProvider");
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // Uses correct type
        CorsConfiguration configuration = new CorsConfiguration(); // Uses correct type
        configuration.setAllowedOrigins(List.of("*")); // Configure allowed origins properly
        configuration.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        configuration.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "Refresh-Token")); // Use literal or SecurityConstants.ALLOWED_HEADERS if available
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Uses correct type
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
