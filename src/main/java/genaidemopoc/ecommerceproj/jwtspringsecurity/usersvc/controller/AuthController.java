package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.LoginRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(AppConstants.AUTH_BASE_PATH)
@Tag(name = "Authentication", description = AppConstants.API_LOGIN_USER_DESC)
@Validated
public class AuthController {

	private static final Logger authControllerLogger = LoggerFactory.getLogger(AuthController.class);
	private final AuthService authService;

	public AuthController(AuthService service) {
		this.authService = service;
	}

	@Operation(
		summary = AppConstants.REGISTER_USER,
		description = AppConstants.API_REGISTER_USER_DESC
	)
	@ApiResponse(
		responseCode = "201",
		description = AppConstants.USER_REGISTERED_SUCCESS
	)
	@ApiResponse(
		responseCode = "400",
		description = AppConstants.BAD_REQUEST
	)
	@PostMapping("/user/register")
	public ResponseEntity<AuthResponse> registerUser(
		@Valid @RequestBody UserRegisterRequest request,
		HttpServletResponse response
	) {
		authControllerLogger.info("Received user registration request for email: {}", request.getEmail());
		AuthResponse createdUserResponse = authService.registerUser(request, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserResponse);
	}

	@Operation(
		summary = "Refresh access token",
		description = AppConstants.API_REFRESH_TOKEN_DESC
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AppConstants.TOKEN_REFRESHED_SUCCESS),
		@ApiResponse(responseCode = "401", description = AppConstants.REFRESH_TOKEN_INVALID),
		@ApiResponse(responseCode = "500", description = AppConstants.INTERNAL_SERVER_ERROR)
	})
	@PostMapping("/refresh-token")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(value = SecurityConstants.REFRESH_TOKEN_COOKIE, required = false) 
			@NotBlank(message = "Refresh token cookie is required") String refreshToken,
			HttpServletResponse response) {
		AuthResponse authResponse = authService.refreshAccessTokens(refreshToken, response);
		return ResponseEntity.ok(authResponse);
	}

	@Operation(
		summary = "Logout user",
		description = AppConstants.API_LOGOUT_DESC,
		security = { @SecurityRequirement(name = SecurityConstants.AUTHORIZATION) }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AppConstants.USER_LOGOUT_SUCCESS),
		@ApiResponse(responseCode = "401", description = SecurityConstants.ERROR_INVALID_TOKEN),
		@ApiResponse(responseCode = "500", description = AppConstants.INTERNAL_SERVER_ERROR)
	})
	@PostMapping("/logout")
	public ResponseEntity<String> logout(
			@RequestHeader(SecurityConstants.AUTHORIZATION) 
			@NotBlank(message = "Authorization header is required") String authHeader) {
		authService.logout(authHeader);
		return ResponseEntity.ok(AppConstants.USER_LOGOUT_SUCCESS);
	}

	@Operation(
		summary = "Logout from all devices",
		description = AppConstants.API_LOGOUT_ALL_DESC,
		security = { @SecurityRequirement(name = SecurityConstants.AUTHORIZATION) }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = AppConstants.USER_LOGOUT_ALL_SUCCESS),
		@ApiResponse(responseCode = "401", description = SecurityConstants.ERROR_INVALID_TOKEN),
		@ApiResponse(responseCode = "500", description = AppConstants.INTERNAL_SERVER_ERROR)
	})
	@PostMapping("/logout-all")
	public ResponseEntity<String> logoutAll(
			@CookieValue(name = SecurityConstants.REFRESH_TOKEN_COOKIE, required = false) String cookieRefreshToken,
			@RequestHeader(value = "Refresh-Token", required = false) String headerRefreshToken,
			@RequestParam(value = "refreshToken", required = false) String paramRefreshToken)
		{
		String refreshToken = cookieRefreshToken;
		if (refreshToken == null || refreshToken.isBlank()) {
			refreshToken = headerRefreshToken;
		}
		if (refreshToken == null || refreshToken.isBlank()) {
			refreshToken = paramRefreshToken;
		}
		
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new jakarta.validation.ConstraintViolationException("Refresh token must be provided via cookie, header, or parameter", null);
		}
		authService.logoutAll(refreshToken);
		return ResponseEntity.ok(AppConstants.USER_LOGOUT_ALL_SUCCESS);
	}
}
