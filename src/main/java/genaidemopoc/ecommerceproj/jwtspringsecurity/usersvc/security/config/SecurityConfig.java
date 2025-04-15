package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config;

// Spring Framework
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;
import java.util.List;

// Project Specific
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.authprovider.CustomAuthenticationProvider;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.filter.JWTAuthenticationFilter;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.filter.JWTTokenVerificationFilter;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config.SecurityProperties;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.RefreshTokenService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;

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
    private final JWTUtil jwtUtil;
    private final CustomAuthenticationProvider authenticationProvider;
    private final MeterRegistry meterRegistry;
    private final SecurityProperties securityProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JWTTokenVerificationFilter jwtTokenVerificationFilter;

    private Counter authSuccessCounter;
    private Counter authFailureCounter;

    // Define Auth Whitelist URLs here for now
    private static final String[] AUTH_WHITELIST_URLS = {
        SecurityConstants.AUTH_ENDPOINT,
        AppConstants.AUTH_BASE_PATH + "/test/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/actuator/health"
    };

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

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
     * Creates JWT authentication filter for login processing
     */
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        JWTAuthenticationFilter filter = new JWTAuthenticationFilter(
            authenticationManager(),
            jwtUtil,
            refreshTokenService,
            userRepository,
            passwordEncoder
        );
        filter.setAuthenticationManager(authenticationManager());
        return filter;
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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers -> {
                SecurityProperties.Headers headerProps = securityProperties.getHeaders();
                if (headerProps.getDenyFrameOptions()) {
                    headers.frameOptions(frameOptions -> frameOptions.deny());
                }
                if (headerProps.getEnableHsts()) {
                    headers.httpStrictTransportSecurity(hsts -> hsts
                        .maxAgeInSeconds(headerProps.getHstsMaxAge())
                        .includeSubDomains(headerProps.getHstsIncludeSubDomains()));
                }
                if (headerProps.getEnableXssProtection()) {
                     headers.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));
                }
                 headers.contentSecurityPolicy(csp -> csp.policyDirectives(headerProps.getContentSecurityPolicy()));
                 headers.referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.valueOf(headerProps.getReferrerPolicy().replace("-", "_").toUpperCase())));
            })
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST_URLS).permitAll()
                .requestMatchers(SecurityConstants.ADMIN_ENDPOINT, "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterAt(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtTokenVerificationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS settings for the application.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        SecurityProperties.Cors corsProps = securityProperties.getCors();
        
        configuration.setAllowedOrigins(corsProps.getAllowedOrigins());
        configuration.setAllowedMethods(corsProps.getAllowedMethods());
        configuration.setAllowedHeaders(corsProps.getAllowedHeaders());
        configuration.setExposedHeaders(corsProps.getExposedHeaders());
        configuration.setAllowCredentials(corsProps.getAllowCredentials());
        configuration.setMaxAge(corsProps.getMaxAge());
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
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
