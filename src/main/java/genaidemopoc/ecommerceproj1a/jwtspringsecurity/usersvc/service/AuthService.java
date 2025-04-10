package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.LoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	/**
	 * Authenticate User & Generate JWT (Access + Refresh Tokens)
	 */
	AuthResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response);

	/**
	 * Register a New User
	 */
	AuthResponse registerUser(UserRegisterRequest registerRequest);

	AuthResponse refreshAccessTokens(String oldRefreshToken, HttpServletResponse response);

	void logout(String accessToken);

	void logoutAll(String refreshToken); 
	
	/**
	 * Login user and return access and refresh tokens
	 */
	TokenResponse login(LoginRequest request);
	
	/**
	 * Refresh access token using refresh token
	 */
	TokenResponse refreshToken(String refreshToken);
}
