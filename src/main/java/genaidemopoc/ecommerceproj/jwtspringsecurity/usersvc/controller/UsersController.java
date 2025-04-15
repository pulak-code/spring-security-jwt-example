package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserSelfEditRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.UnauthorizedAccessException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping(AppConstants.API_USERS_URL_ENDPOINT)
@Tag(name = "User Management", description = "Operations for regular user management and profile access")
@Validated
public class UsersController {

	private final UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

	public UsersController(UserService service) {
		this.userService = service;
	}

	@Operation(
		summary = AppConstants.GET_USER_BY_ID, 
		description = AppConstants.FETCH_A_USER_USING_THEIR_ID,
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User details retrieved successfully", 
			content = @Content(schema = @Schema(implementation = UserEntity.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires user role"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PreAuthorize("hasAuthority(SecurityConstants.ROLE_USER)")
	@GetMapping(AppConstants.USER_ENDPOINT + "/{id}")
	public ResponseEntity<UserEntity> getUserById(@PathVariable @NotBlank(message = "User ID cannot be blank") String id) {
		logger.info("Fetching user by ID: {}", id);
		UserEntity user = userService.getUserById(id);
		return ResponseEntity.ok(user);
	}

	@Operation(
		summary = "Edit User Details", 
		description = "Edit user details (user only)",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User details updated successfully", 
			content = @Content(schema = @Schema(implementation = UserEntity.class))),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires user role"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PreAuthorize("hasAuthority(SecurityConstants.ROLE_USER)")
	@PatchMapping(AppConstants.USER_ENDPOINT + "/{id}")
	public ResponseEntity<UserEntity> editUserDetails(
			@PathVariable @NotBlank(message = "User ID cannot be blank") String id,
			@RequestBody @Valid UserSelfEditRequest request) {
		logger.info("Editing user details for ID: {}", id);
		UserEntity updatedUser = userService.editUserDetails(id, request);
		return ResponseEntity.ok(updatedUser);
	}

	@Operation(
		summary = "Get User Profile", 
		description = "Retrieves user profile (own or admin access only)",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Profile retrieved successfully", 
			content = @Content(schema = @Schema(implementation = UserEntity.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - can only view own profile or requires admin role"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	@GetMapping("/profile/{userId}")
	@PreAuthorize("hasAuthority(SecurityConstants.ROLE_USER) or hasAuthority(SecurityConstants.ROLE_ADMIN)")
	public UserEntity getUserProfile(
			@PathVariable @NotBlank(message = "User ID cannot be blank") String userId,
			Principal principal) {
		UserEntity requestedUser = userService.getUserById(userId);
		// Fetch the currently logged-in user based on Principal (email)
		UserEntity loggedInUser = userService.getUserByEmail(principal.getName());
		// If the logged-in user is NOT an admin AND they're trying to access someone
		// else's profile → DENY ACCESS
		if (!loggedInUser.getRoles().contains(SecurityConstants.ROLE_ADMIN) && !loggedInUser.getId().equals(requestedUser.getId())) {
			throw new UnauthorizedAccessException("You are not authorized to view this profile.");
		}
		return requestedUser;
	}
}
