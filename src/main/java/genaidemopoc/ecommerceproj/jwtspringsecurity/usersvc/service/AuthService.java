package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.LoginRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	/**
	 * Register a New User
	 */
	AuthResponse registerUser(UserRegisterRequest registerRequest, HttpServletResponse response);

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
