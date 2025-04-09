package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import static genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.AppConstants.*;

@RestController
@RequestMapping(AUTH_BASE_PATH)
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

	private static final Logger authControllerLogger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	public AuthController(AuthService service) {
		this.authService = service;
	}

	@Operation(
		summary = "Register new user",
		description = "Register a new user with email and password"
	)
	@ApiResponse(
		responseCode = "200",
		description = "User successfully registered"
	)
	@ApiResponse(
		responseCode = "400",
		description = "Invalid input data"
	)
	@PostMapping("/user/register")
	public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
		authControllerLogger.info("Received user registration request for email: {}", request.getEmail());
		AuthResponse createdUser = authService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@Operation(
		summary = UserServiceConstants.USER_LOGIN, 
		description = UserServiceConstants.AUTHENTICATES_A_USER_AND_RETURNS_JWT_TOKEN,
		tags = {"Authentication"}
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Authentication successful"),
		@ApiResponse(responseCode = "401", description = "Invalid credentials"),
		@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping(UserServiceConstants.USER_ENDPOINT + UserServiceConstants.USER_LOGIN_ENDPOINT)
	public ResponseEntity<AuthResponse> authenticateUser(@RequestBody UserLoginRequest loginRequest,
			HttpServletResponse response) {
		AuthResponse authResponse = authService.authenticateUser(loginRequest, response);
		// Generate JWT token
	    String jwtToken = authResponse.getAccessToken();
	    // Set token in the response header (optional, for frontend convenience)
	    response.setHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + jwtToken);

	    // Return the token in the response body as well
	    return ResponseEntity.ok(authResponse);
	}

	@Operation(
		summary = "Refresh access token", 
		description = "Generates a new access token using the refresh token",
		tags = {"Authentication"}
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
		@ApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
		@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(value = SecurityConstants.REFRESH_TOKEN_COOKIE, required = false) String refreshToken, HttpServletResponse response) {
		if (refreshToken == null)
			throw new InvalidJWTTokenException(MessageUserServiceConstants.REFRESH_TOKEN_INVALID);
		AuthResponse authResponse = authService.refreshAccessTokens(refreshToken, response);
		return ResponseEntity.ok(authResponse);
	}

	@Operation(
		summary = "Logout user", 
		description = "Invalidates the current access token by adding it to the blacklist",
		security = { @SecurityRequirement(name = "Bearer Authentication") },
		tags = {"Authentication"}
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Logged out successfully"),
		@ApiResponse(responseCode = "401", description = "Invalid or missing token"),
		@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader(UserServiceConstants.AUTHORIZATION) String authHeader) {
		authService.logout(authHeader);
		return ResponseEntity.ok(MessageUserServiceConstants.LOGOUT_SUCCESS);
	}

	@Operation(
		summary = "Logout from all devices", 
		description = "Invalidates all sessions by blacklisting all tokens",
		security = { @SecurityRequirement(name = "Bearer Authentication") },
		tags = {"Authentication"}
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Logged out from all devices successfully"),
		@ApiResponse(responseCode = "401", description = "Invalid or missing token"),
		@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	/**
	 * Logs out a user from all devices by invalidating all refresh tokens.
	 * This endpoint accepts the refresh token in multiple ways:
	 * 1. From an HTTP-only cookie (for browser clients)
	 * 2. From a "Refresh-Token" header (for API clients)
	 * 3. From a URL parameter named "refreshToken" (alternative method)
	 *
	 * @param cookieRefreshToken Refresh token from HTTP-only cookie
	 * @param headerRefreshToken Refresh token from request header
	 * @param paramRefreshToken Refresh token from URL parameter
	 * @return ResponseEntity with success message or error response
	 */
	@PostMapping("/logout-all")
	public ResponseEntity<String> logoutAll(
			@CookieValue(name = SecurityConstants.REFRESH_TOKEN_COOKIE, required = false) String cookieRefreshToken,
			@RequestHeader(value = "Refresh-Token", required = false) String headerRefreshToken,
			@RequestParam(value = "refreshToken", required = false) String paramRefreshToken)
		//	,@RequestHeader(value = UserServiceConstants.AUTHORIZATION, required = false) String authHeader) {
		{
		// Try to get refresh token from any available source
		String refreshToken = cookieRefreshToken;
		
		// If not in cookie, check header
		if (refreshToken == null || refreshToken.isBlank()) {
			refreshToken = headerRefreshToken;
		}
		
		// If not in header, check request parameter
		if (refreshToken == null || refreshToken.isBlank()) {
			refreshToken = paramRefreshToken;
		}
		
		// If still not found, check for access token in Authorization header
		/*if ((refreshToken == null || refreshToken.isBlank()) && authHeader != null && authHeader.startsWith("Bearer ")) {
			// If no refresh token found but we have an access token, use it
			String accessToken = authHeader.substring(7);
			authService.logout(authHeader); // Use the regular logout with the auth header
			return ResponseEntity.ok("Logged out from all devices using current session token");
		}
		*/
		// If no token found at all
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new InvalidJWTTokenException(MessageUserServiceConstants.TOKEN_MISSING);
		}
		
		authService.logoutAll(refreshToken);
		return ResponseEntity.ok("Logged out from all devices");
	}

}
