package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.authprovider.JwtAuthenticationProvider;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter.CorsFilter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter.JWTAuthenticationFilter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	// Inject the custom user details service (loads user info from DB)
	// @Lazy
	private CustomUserDetailsService userDetailsService;
	// Inject the custom JWT authentication filter for processing JWT tokens
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	private final JWTUtil JwtUtil;
	public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter, JWTUtil JwtUtil) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.JwtUtil = JwtUtil;
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepo) {
		return new CustomUserDetailsService(userRepo);
	}

	// Constructor injection ensures that these beans are provided by Spring
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Disable CSRF protection for stateless REST APIs using JWT
				.csrf(csrf -> csrf.disable())
				// Configure authorization rules for different endpoints
				.authorizeHttpRequests(auth -> auth
						// Permit all requests to authentication endpoints
						.requestMatchers(SecurityConstants.URL_ALL).permitAll()
						.requestMatchers(
				                "/swagger-ui/**",
				                "/swagger-ui.html",
				                "/v3/api-docs/**",
				                "/v3/api-docs.yaml",
				                "/test/**",  // Allow all test endpoints
				                "/test/auth/register",
				                "/test/auth/login",
				                "/test/user/profile",
				                "/rest-test/**", // Allow all rest-test endpoints
				                "/rest-test/status", 
				                "/rest-test/auth/register",
				                "/rest-test/auth/login",
				                "/rest-test/user/profile",
				                "/debug/**",  // Allow all debug endpoints
				                "/debug/status",
				                "/debug/register",
				                "/debug/login"
				            ).permitAll()
						.requestMatchers("/api/auth/user/register", "/api/auth/admin/register", 
						                 "/api/auth/user/login", "/api/auth/admin/login",
						                 // Also permit these variations for backward compatibility
						                 "/api/auth/register", "/api/auth/login").permitAll()
						// Restrict /admin/** endpoints to users with ROLE_ADMIN
						.requestMatchers(SecurityConstants.URL_ALL_ADMIN).hasRole("ADMIN")
						// Restrict /user/** endpoints to users with ROLE_USER
						.requestMatchers(SecurityConstants.URL_ALL_USER).hasRole("USER")
						// All other endpoints require authentication
						.requestMatchers(EndpointRequest.to(HealthEndpoint.class)).permitAll() // Public health check
						.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN") // Protect all
						// other Actuator endpoints
						.anyRequest().authenticated())
				// Enforce stateless session management (no server-side session)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Set the custom authentication provider for DB-backed authentication
				//.authenticationProvider(authenticationProvider(userDetailsService, null))
				// Add the CORS filter to process tokens before the jwt auth filter n then
				// standard authentication
				.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
				// Add the JWT filter to process tokens before the standard authentication
				// filter
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		// Build and return the security filter chain
		return http.build();
	}

	// returns a bean of CORS filter
	@Bean
	CorsFilter corsFilter() {
		return new CorsFilter();
	}

//	@Bean
//	AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
//			PasswordEncoder passwordEncoder) {
//		// Create a DAO authentication provider to retrieve user details from the DB
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(userDetailsService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}

	@Bean
    AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userDetailsService);
        daoAuthProvider.setPasswordEncoder(passwordEncoder);
        JwtAuthenticationProvider jwtAuthProvider = new JwtAuthenticationProvider(userDetailsService, JwtUtil);
        return new ProviderManager(List.of(daoAuthProvider, jwtAuthProvider));
    }
	/*
	 * DaoAuthenticationProvider retrieves user details from UserDetailsService and
	 * validates credentials
	 * 
	 * InMemoryAuthenticationProvider Stores users in memory (via
	 * InMemoryUserDetailsManager)
	 * 
	 * LdapAuthenticationProvider Authenticates users via an LDAP server (e.g.,
	 * Active Directory)
	 * 
	 * OAuth2AuthenticationProvider Handles authentication via OAuth2 (e.g., Google,
	 * GitHub)
	 * 
	 * JwtAuthenticationProvider uses JWT tokens for stateless authentication
	 */

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(9); // here 9 is log rounds/cost factor/bcrypt strength, which determines how
		// many times the hashing algorithm, it runs slightly more speed n
		// slightly less secure than 10
	}

	/*
	 * Encoder Strength Pros Cons Use Case
	 * 
	 * BCryptPasswordEncoder Strong Secure, uses a random salt, resists brute-force
	 * attacks Slightly slower (intentional for security) Recommended for most
	 * applications
	 * 
	 * Argon2PasswordEncoder Very Strong Advanced key derivation function, resists
	 * side-channel attacks Requires extra dependencies If high security is needed
	 * (e.g., banking apps)
	 * 
	 * PBKDF2PasswordEncoder Strong Used in NIST standards, good resistance against
	 * brute-force Can be slower if iteration count is high High-security
	 * applications
	 * 
	 * SCryptPasswordEncoder Very Strong Memory-intensive, good against GPU attacks
	 * Requires extra resources If memory-hardness is needed
	 * 
	 * MessageDigestPasswordEncoder (MD5, SHA-256) Weak Fast Not secure against
	 * brute-force attacks ❌ Not recommended NoOpPasswordEncoder ❌ No security Just
	 * returns plain text No encryption! Only for testing (Not for production)
	 */
}
