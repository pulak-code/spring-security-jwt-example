package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.crypto.password.PasswordEncoder;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Admin operations for user management and system administration")
@RequiredArgsConstructor
@Validated
public class AdminController {

	private final AdminService adminService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Operation(
		summary = "Admin Dashboard", 
		description = "Displays welcome message for admin dashboard",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Welcome message displayed"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dashboard")
	public ResponseEntity<String> adminDashboard() {
		return ResponseEntity.ok("Welcome, Admin!");
	}

	@Operation(
		summary = "Update User by Admin", 
		description = "Allows an admin to update user details",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User updated successfully"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(AppConstants.USER_ENDPOINT + "update/{userid}")
	public ResponseEntity<UserEntity> updateUser(
			@PathVariable @NotBlank(message = "User ID cannot be blank") String userid, 
			@RequestBody @Valid UserUpdateRequest request,
			Principal principal) {
		UserEntity updatedUser = adminService.updateUser(userid, request, principal);
		return ResponseEntity.ok(updatedUser);
	}

	@Operation(
		summary = "Get All Users", 
		description = "Retrieves a list of all users",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(AppConstants.USER_ENDPOINT)
	public ResponseEntity<List<UserEntity>> getUsers() {
		List<UserEntity> users = adminService.getAllUsers();
		logger.debug("GET /api/admin/users endpoint called, returning {} users", users.size());
		return ResponseEntity.ok(users);
	}

	@Operation(
		summary = "Get All Users (Alternative Endpoint)", 
		description = "Retrieves a list of all users",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(AppConstants.USER_ENDPOINT + "/all")
	public ResponseEntity<List<UserEntity>> getAllUsers() {
		List<UserEntity> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	@Operation(
		summary = "Delete All Users", 
		description = "Deletes all users except the currently logged-in admin",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "All users deleted successfully")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(AppConstants.USER_ENDPOINT + "/all")
	public ResponseEntity<String> deleteAllUsers(Principal principal) {
		adminService.deleteAllUsers(principal);
		return ResponseEntity.ok("All users deleted successfully except current admin");
	}

	@Operation(
		summary = "Search User", 
		description = "Searches for users based on email or username",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(AppConstants.USER_ENDPOINT + "/search")
	public ResponseEntity<List<UserEntity>> searchUser(
			@RequestParam(required = false) @Size(max = 100, message = "Email search term too long") String email,
			@RequestParam(required = false) @Size(max = 100, message = "Username search term too long") String username) {
		List<UserEntity> users = adminService.searchUser(email, username);
		return ResponseEntity.ok(users);
	}

	@Operation(
		summary = "Create Admin User", 
		description = "Creates a new user with ROLE_ADMIN. Defaults to admin@example.com if no details provided.",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Admin user created successfully"),
		@ApiResponse(responseCode = "400", description = "Invalid input"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role"),
		@ApiResponse(responseCode = "409", description = "User already exists")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users")
	public ResponseEntity<Map<String, Object>> createAdminUser(@RequestBody(required = false) Map<String, String> request) {
		Map<String, Object> response = new HashMap<>();
		String email = request != null && request.containsKey("email") ? request.get("email") : "admin@example.com";
		String password = request != null && request.containsKey("password") ? request.get("password") : "Password@1234";
		String name = request != null && request.containsKey("name") ? request.get("name") : "Admin User";
		
		logger.info("Attempting to create admin user: {}", email);
		
		try {
			Optional<UserEntity> existingUser = userRepository.findByEmail(email);
			
			if (existingUser.isPresent()) {
				response.put("userExists", true);
				response.put("message", "User with email '" + email + "' already exists");
				logger.warn("Attempt to create already existing user: {}", email);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}
			
			UserEntity user = new UserEntity();
			user.setEmail(email);
			user.setPassword(passwordEncoder.encode(password));
			user.setName(name);
			
			List<String> roles = new ArrayList<>();
			roles.add(SecurityConstants.ROLE_ADMIN);
			user.setRoles(roles);
			
			UserEntity savedUser = userRepository.save(user);
			
			response.put("userCreated", true);
			response.put("userId", savedUser.getId());
			response.put("email", savedUser.getEmail());
			response.put("name", savedUser.getName());
			response.put("roles", savedUser.getRoles());
			
			logger.info("Admin user created successfully: {}", savedUser.getEmail());
			
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			logger.error("Error creating admin user '{}': {}", email, e.getMessage(), e);
			response.put("error", "Failed to create admin user: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Operation(
		summary = "Reset Admin Passwords", 
		description = "Resets passwords for default admin accounts (admin@example.com, admin99@example.com) to 'Password@1234'",
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Admin passwords reset successfully"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role"),
		@ApiResponse(responseCode = "500", description = "Internal server error during reset")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/reset-password")
	public ResponseEntity<Map<String, Object>> resetAdminPasswords() {
		Map<String, Object> response = new HashMap<>();
		logger.info("Attempting to reset default admin passwords");
		
		try {
			String rawPassword = "Password@1234";
			
			// Reset admin@example.com
			try {
				Optional<UserEntity> adminOpt = userRepository.findByEmail("admin@example.com");
				if (adminOpt.isPresent()) {
					UserEntity admin = adminOpt.get();
					String encodedPassword = passwordEncoder.encode(rawPassword);
					admin.setPassword(encodedPassword);
					if (admin.getRoles() == null) admin.setRoles(new ArrayList<>());
					if (!admin.getRoles().contains(SecurityConstants.ROLE_ADMIN)) admin.getRoles().add(SecurityConstants.ROLE_ADMIN);
					userRepository.save(admin);
					response.put("adminResetSuccess", true);
					logger.info("Password reset for admin@example.com");
				} else {
					response.put("adminResetSuccess", false);
					response.put("adminError", "User admin@example.com not found");
					logger.warn("User admin@example.com not found for password reset");
				}
			} catch (Exception e) {
				logger.error("Error resetting password for admin@example.com: {}", e.getMessage(), e);
				response.put("adminResetSuccess", false);
				response.put("adminError", "Error resetting password: " + e.getMessage());
			}
			
			// Reset admin99@example.com
			try {
				Optional<UserEntity> admin99Opt = userRepository.findByEmail("admin99@example.com");
				if (admin99Opt.isPresent()) {
					UserEntity admin99 = admin99Opt.get();
					String encodedPassword = passwordEncoder.encode(rawPassword);
					admin99.setPassword(encodedPassword);
					if (admin99.getRoles() == null) admin99.setRoles(new ArrayList<>());
					if (!admin99.getRoles().contains(SecurityConstants.ROLE_ADMIN)) admin99.getRoles().add(SecurityConstants.ROLE_ADMIN);
					userRepository.save(admin99);
					response.put("admin99ResetSuccess", true);
					logger.info("Password reset for admin99@example.com");
				} else {
					response.put("admin99ResetSuccess", false);
					response.put("admin99Error", "User admin99@example.com not found");
					logger.warn("User admin99@example.com not found for password reset");
				}
			} catch (Exception e) {
				logger.error("Error resetting password for admin99@example.com: {}", e.getMessage(), e);
				response.put("admin99ResetSuccess", false);
				response.put("admin99Error", "Error resetting password: " + e.getMessage());
			}
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Unexpected error during admin password reset process: {}", e.getMessage(), e);
			response.put("error", "Failed to reset admin passwords: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}