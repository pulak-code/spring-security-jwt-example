package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "Admin operations for user management and system administration")
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService service) {
		this.adminService = service;
	}

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
		summary = "Update User", 
		description = "Updates user details (admin only)",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User updated successfully", 
			content = @Content(schema = @Schema(implementation = UserEntity.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role"),
		@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(UserServiceConstants.USER_ENDPOINT + "update/{userid}")
	public ResponseEntity<UserEntity> updateUser(@PathVariable String userid, @RequestBody UserUpdateRequest request,
			Principal principal) {
		UserEntity updatedUser = adminService.updateUser(userid, request, principal);
		return ResponseEntity.ok(updatedUser);
	}
	@Operation(
		summary = "Get All Users", 
		description = "Fetches a list of all users (admin only)",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "List of all users retrieved successfully"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(UserServiceConstants.USER_ENDPOINT+ "/all")
	public ResponseEntity<List<UserEntity>> getAllUsers() {
		List<UserEntity> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	@Operation(
		summary = "Delete All Users", 
		description = "Deletes all users from the system except the current admin",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "All users deleted successfully except current admin"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(UserServiceConstants.USER_ENDPOINT + "/all")
	public ResponseEntity<String> deleteAllUsers(Principal principal) {
		adminService.deleteAllUsers(principal);
		return ResponseEntity.ok("All users deleted successfully except current admin");
	}

	@Operation(
		summary = "Search Users", 
		description = "Searches for users by email or username (admin only)",
		security = { @SecurityRequirement(name = "Bearer Authentication") }
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
		@ApiResponse(responseCode = "401", description = "Unauthorized"),
		@ApiResponse(responseCode = "403", description = "Forbidden - requires admin role")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(UserServiceConstants.USER_ENDPOINT + "/search")
	public ResponseEntity<List<UserEntity>> searchUser(@RequestParam(required = false) String email,
			@RequestParam(required = false) String username) {
		List<UserEntity> users = adminService.searchUser(email, username);
		return ResponseEntity.ok(users);
	}
}