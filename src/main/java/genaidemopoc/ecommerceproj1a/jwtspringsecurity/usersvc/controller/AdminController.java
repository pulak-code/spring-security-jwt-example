package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Management", description = "Admin operations for user management and system administration")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

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
	public ResponseEntity<UserEntity> updateUser(@PathVariable String userid, @RequestBody UserUpdateRequest request,
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
	public ResponseEntity<List<UserEntity>> searchUser(@RequestParam(required = false) String email,
			@RequestParam(required = false) String username) {
		List<UserEntity> users = adminService.searchUser(email, username);
		return ResponseEntity.ok(users);
	}
}