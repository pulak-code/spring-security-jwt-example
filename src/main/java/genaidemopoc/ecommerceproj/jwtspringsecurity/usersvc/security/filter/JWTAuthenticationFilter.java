package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.RefreshToken;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			RefreshTokenService refreshTokenService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.refreshTokenService = refreshTokenService;
		this.objectMapper = new ObjectMapper();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		logger.debug("Attempting authentication for login request");
		
		try {
			// Extract credentials from request
			LoginCredentials creds = objectMapper.readValue(request.getInputStream(), LoginCredentials.class);
			String email = creds.getEmail();
			String password = creds.getPassword();
			
			logger.debug("Login attempt for email: {}", email);
			
			// Create auth token with credentials
			UsernamePasswordAuthenticationToken authRequest = 
				new UsernamePasswordAuthenticationToken(email, password);
			
			// Authenticate using the manager (this will call UserDetailsService)
			Authentication auth = this.authenticationManager.authenticate(authRequest);
			logger.info("Authentication successful for user: {}", email);
			return auth;
			
		} catch (IOException e) {
			logger.error("Failed to parse authentication request", e);
			throw new BadCredentialsException("Invalid authentication request format");
		} catch (BadCredentialsException e) {
			logger.warn("Bad credentials for login attempt: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Authentication error", e);
			throw new BadCredentialsException("Authentication failed: " + e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		logger.debug("Successful authentication. Generating tokens.");
		
		User user = (User) authResult.getPrincipal();
		String username = user.getUsername();
		
		// Generate tokens
		String accessToken = jwtUtil.generateAccessToken(username);
		String refreshTokenValue = jwtUtil.generateRefreshToken(username);
		
		// Save refresh token
		Optional<UserEntity> userOpt = userRepository.findByEmail(username);
		if (userOpt.isPresent()) {
			UserEntity userEntity = userOpt.get();
			refreshTokenService.createRefreshToken(
				userEntity.getId(),
				username,
				refreshTokenValue,
				Instant.now().plusSeconds(jwtUtil.getRefreshTokenExpirationSeconds())
			);
		}
		
		// Set the Authorization header
		response.setHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + accessToken);
		
		// Set the refresh token cookie
		Cookie refreshTokenCookie = new Cookie("refresh_token", refreshTokenValue);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setPath(SecurityConstants.REFRESH_TOKEN_ENDPOINT);
		refreshTokenCookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(SecurityConstants.REFRESH_TOKEN_EXPIRATION_TIME));
		response.addCookie(refreshTokenCookie);
		
		// Prepare response body
		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("accessToken", accessToken);
		tokenMap.put("refreshToken", refreshTokenValue);
		
		// Write response
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), tokenMap);
		
		logger.info("Authentication tokens generated for user: {}", username);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		logger.warn("Unsuccessful authentication: {}", failed.getMessage());
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("error", "Invalid email or password");
		errorDetails.put("message", failed.getMessage());
		
		objectMapper.writeValue(response.getWriter(), errorDetails);
	}

	public static class LoginCredentials {
		private String email;
		private String password;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
} 