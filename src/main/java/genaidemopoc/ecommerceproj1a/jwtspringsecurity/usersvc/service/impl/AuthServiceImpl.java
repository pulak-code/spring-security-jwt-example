package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.LoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AuthService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.RefreshTokenService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger authServiceLogger = LoggerFactory.getLogger(AuthServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Override
	public AuthResponse authenticateUser(UserLoginRequest loginRequest, HttpServletResponse response) {
		authServiceLogger.info("Authenticating user: {}", loginRequest.getEmail());

		// Authenticate user
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		// Generate tokens
		String accessToken = jwtUtil.generateAccessToken(loginRequest.getEmail());
		String refreshToken = jwtUtil.generateRefreshToken();

		// Get user details
		UserEntity user = userService.getUserByEmail(loginRequest.getEmail());

		// Save refresh token
		refreshTokenService.saveRefreshToken(refreshToken, user.getId(), user.getEmail());

		// Create refresh token cookie
		Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true); // Use in production with HTTPS
		refreshTokenCookie.setPath("/api/auth/refresh");
		refreshTokenCookie.setMaxAge((int) (jwtUtil.getRefreshExpiration())); // Convert to seconds
		response.addCookie(refreshTokenCookie);

		// Return response with access token only
		return new AuthResponse(accessToken, null, user.getId(), user.getEmail(), user.getRoles());
	}

	@Override
	public AuthResponse refreshAccessTokens(String oldRefreshToken, HttpServletResponse response) {
		authServiceLogger.info("Refreshing access tokens");

		// Verify refresh token
		if (!refreshTokenService.verifyExpiration(oldRefreshToken)) {
			throw new InvalidJWTTokenException("Refresh token is invalid or expired");
		}

		// Get user details from refresh token
		UserEntity user = jwtUtil.extractUser(oldRefreshToken);

		// Generate new tokens
		String newAccessToken = jwtUtil.generateAccessToken(user.getEmail());
		String newRefreshToken = jwtUtil.generateRefreshToken();

		// Save new refresh token
		refreshTokenService.saveRefreshToken(newRefreshToken, user.getId(), user.getEmail());

		// Delete old refresh token
		refreshTokenService.deleteToken(oldRefreshToken);

		// Create refresh token cookie
		Cookie refreshTokenCookie = new Cookie("refresh_token", newRefreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true); // Use in production with HTTPS
		refreshTokenCookie.setPath("/api/auth/refresh");
		refreshTokenCookie.setMaxAge((int) (jwtUtil.getRefreshExpiration())); // Convert to seconds
		response.addCookie(refreshTokenCookie);

		// Return response with access token only
		return new AuthResponse(newAccessToken, null, user.getId(), user.getEmail(), user.getRoles());
	}

	@Override
	public void logout(String authHeader) {
		authServiceLogger.info("Logging out user");

		// Extract token from header
		Optional<String> tokenOpt = JWTUtil.preProcessingTokenChecks(Optional.ofNullable(authHeader));
		if (!tokenOpt.isPresent()) {
			throw new InvalidJWTTokenException("Invalid token format");
		}

		String token = tokenOpt.get();

		// Extract user from token
		UserEntity user = jwtUtil.extractUser(token);

		// Revoke all refresh tokens for the user
		refreshTokenService.revokeAllUserTokens(user.getId());
	}

	@Override
	public void logoutAll(String refreshToken) {
		authServiceLogger.info("Logging out user from all devices");

		// Verify refresh token
		if (!refreshTokenService.verifyExpiration(refreshToken)) {
			throw new InvalidJWTTokenException("Refresh token is invalid or expired");
		}

		// Get user details from refresh token
		UserEntity user = jwtUtil.extractUser(refreshToken);

		// Revoke all refresh tokens for the user
		refreshTokenService.revokeAllUserTokens(user.getId());
	}

	@Override
	public AuthResponse registerUser(UserRegisterRequest registerRequest) {
		authServiceLogger.info("Registering new user: {}", registerRequest.getEmail());

		// Check if user already exists
		if (userService.userExists(registerRequest.getEmail())) {
			throw new UserAlreadyExistsException("User already exists");
		}

		// Create new user
		UserEntity user = new UserEntity();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setName(registerRequest.getName());
		
		// Set default role
		user.getRoles().add("ROLE_USER");

		// Save user
		userService.saveUser(user);

		// Generate tokens
		String accessToken = jwtUtil.generateAccessToken(user.getEmail());
		String refreshToken = jwtUtil.generateRefreshToken();

		// Save refresh token
		refreshTokenService.saveRefreshToken(refreshToken, user.getId(), user.getEmail());

		// Return response with access token only
		return new AuthResponse(accessToken, null, user.getId(), user.getEmail(), user.getRoles());
	}

	@Override
	public TokenResponse login(LoginRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);
		
		UserEntity user = userService.getUserByEmail(request.getEmail());
		
		// Generate access token
		String accessToken = jwtUtil.generateAccessToken(user.getEmail());
		
		// Generate refresh token
		String refreshToken = jwtUtil.generateRefreshToken();
		
		// Save refresh token
		refreshTokenService.saveRefreshToken(refreshToken, user.getId(), user.getEmail());
		
		// Return only access token in response
		return new TokenResponse(accessToken, null);
	}

	@Override
	public TokenResponse refreshToken(String refreshToken) {
		// Verify refresh token
		if (!refreshTokenService.verifyExpiration(refreshToken)) {
			throw new InvalidJWTTokenException("Refresh token is invalid or expired");
		}
		
		// Get user details from refresh token
		UserEntity user = jwtUtil.extractUser(refreshToken);
		
		// Generate new access token
		String newAccessToken = jwtUtil.generateAccessToken(user.getEmail());
		
		// Generate new refresh token
		String newRefreshToken = jwtUtil.generateRefreshToken();
		
		// Save new refresh token
		refreshTokenService.saveRefreshToken(newRefreshToken, user.getId(), user.getEmail());
		
		// Delete old refresh token
		refreshTokenService.deleteToken(refreshToken);
		
		// Return only access token in response
		return new TokenResponse(newAccessToken, null);
	}
}
