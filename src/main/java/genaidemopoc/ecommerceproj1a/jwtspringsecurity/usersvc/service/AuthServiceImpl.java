package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.TokenStorageService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;
	private TokenBlacklistService tokenBlacklistService;
	private UserService userService;
	private TokenStorageService tokenStorageService;

	private Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
	private PasswordEncoder passwordEncoder;

	public AuthServiceImpl(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			TokenBlacklistService tokenBlacklistService, UserService userService, PasswordEncoder passwordEncoder,
			TokenStorageService tokenStorageService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.tokenBlacklistService = tokenBlacklistService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.tokenStorageService = tokenStorageService;
	}

	// Authenticate User & Issue Tokens
	@Override
	public AuthResponse authenticateUser(UserLoginRequest loginRequest, HttpServletResponse response) {
		String userEmail = loginRequest.getEmail();
		authenticate(userEmail, loginRequest.getPassword());
		UserEntity user = userService.getUserByEmail(userEmail);
		String jwtToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
		
		log.info(LoggingUserServiceConstants.LOG_AUTH_SUCCESS, userEmail);
		log.info(LoggingUserServiceConstants.LOG_TOKEN_GENERATED, userEmail);
		
		// Store tokens
		tokenStorageService.storeToken(jwtToken, userEmail);
		tokenStorageService.storeRefreshToken(refreshToken, userEmail);
		
		response.addCookie(getRefreshTokenInCookie(refreshToken));
		return new AuthResponse(jwtToken, null, MessageUserServiceConstants.LOGIN_SUCCESS);
	}

	// Refresh Tokens
	@Override
	public AuthResponse refreshAccessTokens(String oldRefreshToken, HttpServletResponse response) {
		validateRefreshToken(oldRefreshToken);
		UserEntity user = jwtUtil.extractUser(oldRefreshToken);
		// Generate new access token
		String newAccessToken = jwtUtil.generateAccessToken(user);

		// Generate rotated refresh token
		String newRefreshToken = jwtUtil.generateRotatedRefreshToken(oldRefreshToken, tokenBlacklistService);
		
		log.info(LoggingUserServiceConstants.LOG_TOKEN_REFRESHED, user.getEmail());
		log.info(LoggingUserServiceConstants.LOG_TOKEN_GENERATED, user.getEmail());
		
		// Store tokens
		tokenStorageService.storeToken(newAccessToken, user.getEmail());
		tokenStorageService.storeRefreshToken(newRefreshToken, user.getEmail());
		
		response.addCookie(getRefreshTokenInCookie(newRefreshToken));

		return new AuthResponse(newAccessToken, null, MessageUserServiceConstants.LOGIN_SUCCESS);
	}

	@Override
	public void logout(String authHeader) {
		String token = JWTUtil.preProcessingTokenChecks(Optional.ofNullable(authHeader))
				.orElseThrow(() -> new InvalidJWTTokenException(MessageUserServiceConstants.TOKEN_INVALID));

		long expiryTime = jwtUtil.getExpiration(token).getTime();
		tokenBlacklistService.blacklistToken(token, expiryTime);
		
		log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, token);
		log.info(LoggingUserServiceConstants.LOG_TOKEN_BLACKLISTED, token);
		
		// Clear tokens from storage
		tokenStorageService.clearTokens();
	}

	@Override
	public void logoutAll(String refreshToken) {
		validateRefreshToken(refreshToken);
		UserEntity user = jwtUtil.extractUser(refreshToken);
		tokenStorageService.clearTokens();
		log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, "all tokens for user: " + user.getEmail());
	}

	// Common Utility Methods
	private void authenticate(String email, String password) {
		UserEntity user = userService.getUserByEmail(email);
		if (user == null) {
		    throw new UsernameNotFoundException(MessageUserServiceConstants.USER_NOT_FOUND + ": " + email);
		}

		// Ensure password matches stored hash
		if (!passwordEncoder.matches(password, user.getPassword())) {
		    throw new BadCredentialsException(MessageUserServiceConstants.INVALID_CREDENTIALS);
		}
		Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(email, password));
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void validateRefreshToken(String refreshToken) {
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new InvalidJWTTokenException(MessageUserServiceConstants.REFRESH_TOKEN_INVALID);
		}
	}

	private Cookie getRefreshTokenInCookie(String newRefreshToken) {
		Cookie refreshCookie = new Cookie(SecurityConstants.REFRESH_TOKEN_COOKIE, newRefreshToken);
		refreshCookie.setHttpOnly(true); // Prevents access via JavaScript (mitigates XSS attacks)
		refreshCookie.setSecure(true); // Ensures it's sent only over HTTPS
		refreshCookie.setPath("/"); // Available across the entire application
		refreshCookie.setMaxAge((int) (jwtUtil.getExpiration(newRefreshToken).getTime()) / 1000); // Convert ms to
		return refreshCookie; // seconds
	}

	@Override
	public AuthResponse registerUser(UserRegisterRequest registerRequest) {
		userService.saveUser(registerRequest); // Uses the request version
		// Return success response
		return new AuthResponse(null, null, MessageUserServiceConstants.USER_CREATED_SUCCESS);
	}
}
