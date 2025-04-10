package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserSelfEditRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserServiceException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final Logger userServiceLogger = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserEntity getUserById(String userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, userId)));
	}

	@Override
	public void deleteUser(String userId) {
		userServiceLogger.info("Deleting user with id: {}", userId);
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		try {
			userRepository.delete(user);
			userServiceLogger.info("User with id: {} deleted successfully", userId);
		} catch (Exception e) {
			throw new UserServiceException("Failed to delete user with id: " + userId, e);
		}
	}

	// Admin update: Full update with PUT mapping – admin can update all fields.
	@Override
	public UserEntity updateUser(String userId, UserUpdateRequest request) {
		userServiceLogger.info("Admin updating user with id: {}", userId);
		UserEntity existingUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
		// Assume updateUser can update all fields (name, email, password, addresses,
		// etc.)
		existingUser.setName(request.getName());
		existingUser.setEmail(request.getEmail());
		// For password update, encode the password if provided (and not empty)
		if (request.getPassword() != null && !request.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		// Assume addresses and any other fields are updated similarly
		existingUser.setAddresses(request.getAddresses());
		try {
			userServiceLogger.info("User with id: {} updated successfully by admin", userId);
			return userRepository.save(existingUser);
		} catch (Exception e) {
			throw new UserServiceException("Failed to update user with id: " + userId, e);
		}
	}

	// Self-edit endpoint: User can update only email, password, and addresses
	@Override
	public UserEntity editUserDetails(String userId, UserSelfEditRequest request) {
		userServiceLogger.info("User {} editing their own details", userId);
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		// Update only allowed fields
		if (request.getEmail() != null && !request.getEmail().isEmpty()) {
			user.setEmail(request.getEmail());
		}
		if (request.getPassword() != null && !request.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		if (request.getAddresses() != null) {
			user.setAddresses(request.getAddresses());
		}
		try {
			userServiceLogger.info("User {} updated their details successfully", userId);
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserServiceException("Failed to edit user details for id: " + userId, e);
		}
	}

	@Override
	public UserEntity getUserByEmail(String email) {
		userServiceLogger.info("Finding user by email");
		return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
	}

	@Override
	public boolean userExists(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserEntity saveUser(UserEntity user) {
		// Check if email already exists
		String email = user.getEmail();
		Optional<UserEntity> existingUser = userRepository.findByEmail(email);
		if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
			throw new UserAlreadyExistsException(String.format(AppConstants.USER_EMAIL_ALREADY_EXISTS, email));
		}
		
		// Assign default role as USER if not set (ensure roles list is initialized)
		if (user.getRoles() == null) {
			user.setRoles(new ArrayList<>());
		}
		if (user.getRoles().isEmpty()) {
			user.setRoles(List.of("ROLE_USER"));
		}

		// Ensure other properties are set properly
		if (user.getName() == null || user.getName().isBlank()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}

		if (user.getAddresses() == null) {
			user.setAddresses(new ArrayList<>()); // Default empty list if null
		}

		if (user.getOrderIds() == null) {
			user.setOrderIds(new ArrayList<>()); // Default empty list if null
		}
		userServiceLogger.debug("Saving user entity: {}", user);
		return userRepository.save(user);
	}

	@Override
	public UserEntity saveUser(UserRegisterRequest registerRequest) {
		Optional<UserEntity> existingUserCheck = userRepository.findByEmail(registerRequest.getEmail());
		if (existingUserCheck.isPresent()) {
			throw new UserAlreadyExistsException(String.format(AppConstants.USER_EMAIL_ALREADY_EXISTS, registerRequest.getEmail()));
		}
		
		UserEntity user = new UserEntity();
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Encode password here
		user.setName(registerRequest.getName());
		
		// Set roles from the registration request if provided, default to USER otherwise
		if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
			user.setRoles(registerRequest.getRoles());
		} else {
			user.setRoles(List.of("ROLE_USER")); // Default role
		}
		
		return userRepository.save(user); // Save the fully prepared entity
	}

}
