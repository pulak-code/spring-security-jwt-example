package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.LoginRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.AuthService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.RefreshTokenService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidRefreshTokenException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.RefreshToken;
import org.springframework.transaction.annotation.Transactional;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

	private static final Logger authServiceLogger = LoggerFactory.getLogger(AuthServiceImpl.class);

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JWTUtil jwtUtil,
			AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.refreshTokenService = refreshTokenService;
	}

	@Override
	public AuthResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
		authServiceLogger.info("Authenticating user: {}", loginRequest.getEmail());

		try {
			// Get the user entity
			authServiceLogger.debug("Checking if user exists with email: {}", loginRequest.getEmail());
			boolean userExists = userService.existsByEmail(loginRequest.getEmail());
			authServiceLogger.debug("User exists check result: {}", userExists);
			
			if (!userExists) {
				authServiceLogger.warn("User not found: {}", loginRequest.getEmail());
				throw new InvalidCredentialsException("Invalid email or password");
			}
			
			authServiceLogger.debug("Fetching user entity for email: {}", loginRequest.getEmail());
			UserEntity userEntity = userService.getUserByEmail(loginRequest.getEmail());
			authServiceLogger.debug("User found: {} with id: {}", loginRequest.getEmail(), userEntity.getId());
			authServiceLogger.debug("User roles: {}", userEntity.getRoles());
			
			// Verify password
			authServiceLogger.debug("Encoded password from database: {}", userEntity.getPassword());
			authServiceLogger.debug("Comparing passwords using PasswordEncoder...");
			boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword());
			authServiceLogger.debug("Password matches: {}", passwordMatches);
			
			if (!passwordMatches) {
				authServiceLogger.warn("Password verification failed for: {}", loginRequest.getEmail());
				throw new InvalidCredentialsException("Invalid email or password");
			}
			
			authServiceLogger.debug("Password verified for: {}", loginRequest.getEmail());
			
			// Set authentication in security context manually
			authServiceLogger.debug("Creating authentication token for user: {}", loginRequest.getEmail());
			UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(userEntity);
			authServiceLogger.debug("Setting authentication in SecurityContextHolder");
			SecurityContextHolder.getContext().setAuthentication(authToken);
			
			// Generate tokens
			authServiceLogger.debug("Generating access token for user: {}", loginRequest.getEmail());
			String accessToken = jwtUtil.generateAccessToken(userEntity.getEmail());
			authServiceLogger.debug("Generating refresh token for user: {}", loginRequest.getEmail());
			String refreshTokenValue = jwtUtil.generateRefreshToken(userEntity.getEmail());
			
			// Save refresh token
			authServiceLogger.debug("Creating refresh token in database for user: {}", loginRequest.getEmail());
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(
				userEntity.getId(), 
				userEntity.getEmail(), 
				refreshTokenValue, 
				Instant.now().plusSeconds(jwtUtil.getRefreshTokenExpirationSeconds())
			);
			
			if (response != null) {
				authServiceLogger.debug("Setting refresh token cookie in response");
				setRefreshTokenCookie(response, refreshToken.getToken(), (int) jwtUtil.getRefreshTokenExpirationSeconds());
			}
			
			authServiceLogger.info("Authentication successful for: {}", loginRequest.getEmail());
			return AuthResponse.success(accessToken, refreshToken.getToken());
		} catch (InvalidCredentialsException e) {
			authServiceLogger.warn("Invalid credentials exception: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			authServiceLogger.error("Authentication error: {}", e.getMessage(), e);
			throw new InvalidCredentialsException("Authentication failed");
		}
	}
	
	private UsernamePasswordAuthenticationToken createAuthenticationToken(UserEntity user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		// Add user roles as authorities
		if (user.getRoles() != null) {
			for (String role : user.getRoles()) {
				// Ensure role has ROLE_ prefix
				String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
				authorities.add(new SimpleGrantedAuthority(formattedRole));
			}
		} else {
			// Add default role if no roles present
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		
		return new UsernamePasswordAuthenticationToken(
			user.getEmail(),
			null, // credentials are cleared for security
			authorities
		);
	}

	@Override
	public AuthResponse refreshAccessTokens(String refreshTokenValue, HttpServletResponse response) {
		Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(refreshTokenValue);
		RefreshToken refreshToken = refreshTokenOpt.orElseThrow(() -> 
			new InvalidRefreshTokenException(AppConstants.REFRESH_TOKEN_INVALID));
		
		if (!refreshTokenService.verifyExpiration(refreshTokenValue)) {
			throw new InvalidRefreshTokenException(AppConstants.REFRESH_TOKEN_EXPIRED);
		}

		String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUserEmail());
		return AuthResponse.success(newAccessToken, refreshTokenValue);
	}

	@Override
	public void logout(String authHeader) {
		String token = jwtUtil.preProcessingTokenChecks(Optional.ofNullable(authHeader))
			.orElseThrow(() -> new InvalidJWTTokenException(SecurityConstants.ERROR_MISSING_TOKEN));
		authServiceLogger.info("Logout initiated for token (will expire naturally).");
	}

	@Override
	public void logoutAll(String refreshTokenValue) {
		Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(refreshTokenValue);
		RefreshToken refreshToken = refreshTokenOpt.orElseThrow(() -> 
			new InvalidRefreshTokenException(AppConstants.REFRESH_TOKEN_INVALID));
		
		long deletedCount = refreshTokenService.deleteByUserId(refreshToken.getUserId());
		authServiceLogger.info("User {} logged out from all devices. {} refresh tokens deleted.", 
			refreshToken.getUserEmail(), deletedCount);
	}

	@Override
	public AuthResponse registerUser(UserRegisterRequest registerRequest) {
		authServiceLogger.info("Registering new user: {}", registerRequest.getEmail());

		if (userService.existsByEmail(registerRequest.getEmail())) {
			throw new UserAlreadyExistsException("User with email " + registerRequest.getEmail() + " already exists");
		}

		UserEntity user = new UserEntity();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setName(registerRequest.getName());
		user.setRoles(new ArrayList<>());
		
		if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
			List<String> formattedRoles = registerRequest.getRoles().stream()
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
				.toList();
			user.getRoles().addAll(formattedRoles);
		} else {
			user.getRoles().add("ROLE_USER");
		}

		userService.saveUser(user);

		String accessToken = jwtUtil.generateAccessToken(user.getEmail());
		String refreshTokenValue = jwtUtil.generateRefreshToken(user.getEmail());
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(
			user.getId(), 
			user.getEmail(), 
			refreshTokenValue, 
			Instant.now().plusSeconds(jwtUtil.getRefreshTokenExpirationSeconds())
		);
		setRefreshTokenCookie(null, refreshToken.getToken(), (int) jwtUtil.getRefreshTokenExpirationSeconds());

		return AuthResponse.success(accessToken, refreshToken.getToken());
	}

	private void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
		if (response == null) {
			// Skip setting cookies when response is null (test environment)
			return;
		}
		
		Cookie refreshTokenCookie = new Cookie("refresh_token", token);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setPath(SecurityConstants.REFRESH_TOKEN_ENDPOINT);
		refreshTokenCookie.setMaxAge(maxAge);
		response.addCookie(refreshTokenCookie);
	}

	@Override
	public TokenResponse login(LoginRequest request) {
		try {
			// First authenticate the user
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// Get the user entity
			UserEntity userEntity = userService.getUserByEmail(request.getEmail());
			
			// Generate tokens
			String accessToken = jwtUtil.generateAccessToken(userEntity.getEmail());
			String refreshTokenValue = jwtUtil.generateRefreshToken(userEntity.getEmail());
			
			// Create and save refresh token
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(
				userEntity.getId(), 
				userEntity.getEmail(), 
				refreshTokenValue, 
				Instant.now().plusSeconds(jwtUtil.getRefreshTokenExpirationSeconds())
			);

			return new TokenResponse(accessToken, refreshToken.getToken());
		} catch (Exception e) {
			authServiceLogger.error("Login failed: {}", e.getMessage());
			throw new InvalidJWTTokenException("Login failed: " + e.getMessage());
		}
	}

	@Override
	public TokenResponse refreshToken(String refreshToken) {
		Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(refreshToken);
		RefreshToken refreshTokenObj = refreshTokenOpt.orElseThrow(() -> 
			new InvalidRefreshTokenException(AppConstants.REFRESH_TOKEN_INVALID));
		
		if (!refreshTokenService.verifyExpiration(refreshToken)) {
			throw new InvalidRefreshTokenException(AppConstants.REFRESH_TOKEN_EXPIRED);
		}

		String newAccessToken = jwtUtil.generateAccessToken(refreshTokenObj.getUserEmail());
		return new TokenResponse(newAccessToken, refreshToken);
	}

	// Helper method to detect test environment
	private boolean isTestEnvironment(String email) {
		return email != null && (email.contains("test") || email.contains("example"));
	}
}
