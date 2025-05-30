package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.impl;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidRoleChangeException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UnauthorizedAccessException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserServiceException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.AdminService;
import jakarta.validation.Valid;

@Service
public class AdminServiceImpl implements AdminService {
	private static final Logger adminServiceLogger = LoggerFactory.getLogger(AdminServiceImpl.class);
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserEntity> searchUser(String email, String adminname) {
		adminServiceLogger.info("Searching admins by email: {} or adminname: {}", email, adminname);
		
		try {
			return userRepository.findByEmailOrNameSafely(email, adminname);
		} catch (Exception e) {
			adminServiceLogger.warn("Error searching users with parameters, falling back to returning all users: {}", e.getMessage());
			return userRepository.findAll();
		}
	}

	@Override
	public void deleteAllUsers(Principal principal) {
		adminServiceLogger.info("Deleting all users except current admin");
		try {
			String currentAdminEmail = principal.getName();
			UserEntity currentAdmin = userRepository.findByEmail(currentAdminEmail)
					.orElseThrow(() -> new UserNotFoundException("Admin user not found: " + currentAdminEmail));
			
			List<UserEntity> allUsers = userRepository.findAll();
			allUsers.stream()
				.filter(user -> !user.getId().equals(currentAdmin.getId()))
				.forEach(userRepository::delete);
			
			adminServiceLogger.info("All users deleted successfully except current admin: {}", currentAdminEmail);
		} catch (Exception e) {
			throw new UserServiceException("Failed to delete users", e);
		}
	}

	@Override
	public List<UserEntity> getAllUsers() {
		adminServiceLogger.info("Fetching all users");
		return userRepository.findAll();
	}

	@Override
	public UserEntity updateUser(String userId, @Valid UserUpdateRequest request, Principal principal) {
		UserEntity existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

		if (request.getName() != null) {
			existingUser.setName(request.getName());
		}
		if (request.getEmail() != null) {
			existingUser.setEmail(request.getEmail());
		}
		if (request.getPassword() != null) {
			existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		existingUser.setAddresses(request.getAddresses());
		existingUser.setOrderIds(request.getOrderIds());

		UserEntity adminUser = userRepository.findByEmail(principal.getName())
				.orElseThrow(() -> new UserNotFoundException("Admin user not found"));

		if (!adminUser.getRoles().contains("ROLE_ADMIN")) {
			throw new UnauthorizedAccessException("You are not authorized to update users");
		}

		if (request.getRoles() != null && !request.getRoles().isEmpty()) {
			if (existingUser.getRoles().contains("ROLE_ADMIN") && !request.getRoles().contains("ROLE_ADMIN")) {
				throw new InvalidRoleChangeException("Cannot demote an admin to user!");
			}
			existingUser.setRoles(request.getRoles());
		}

		return userRepository.save(existingUser);
	}
}
