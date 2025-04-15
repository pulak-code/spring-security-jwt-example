package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserSelfEditRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserAlreadyExistsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserServiceException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger userServiceLogger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserEntity getUserById(String id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, id)));
	}

	@Override
	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_EMAIL, email)));
	}

	@Override
	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserEntity saveUser(UserEntity user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException(String.format(AppConstants.USER_EMAIL_ALREADY_EXISTS, user.getEmail()));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(String id) {
		userServiceLogger.info("Deleting user with id: {}", id);
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, id)));
		try {
			userRepository.delete(user);
			userServiceLogger.info("User with id: {} deleted successfully", id);
		} catch (Exception e) {
			throw new UserServiceException("Failed to delete user with id: " + id, e);
		}
	}

	@Override
	public UserEntity updateUser(String id, UserUpdateRequest request) {
		userServiceLogger.info("Admin updating user with id: {}", id);
		UserEntity existingUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, id)));
		
		existingUser.setName(request.getName());
		existingUser.setEmail(request.getEmail());
		
		if (request.getPassword() != null && !request.getPassword().isEmpty()) {
			existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		
		existingUser.setAddresses(request.getAddresses());
		existingUser.setOrderIds(request.getOrderIds());
		existingUser.setRoles(request.getRoles());
		
		try {
			userServiceLogger.info("User with id: {} updated successfully by admin", id);
			return userRepository.save(existingUser);
		} catch (Exception e) {
			throw new UserServiceException("Failed to update user with id: " + id, e);
		}
	}

	@Override
	public UserEntity editUserDetails(String id, UserSelfEditRequest request) {
		userServiceLogger.info("User {} editing their own details", id);
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_ID, id)));

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
			userServiceLogger.info("User {} updated their details successfully", id);
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserServiceException("Failed to edit user details for id: " + id, e);
		}
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public UserEntity saveUser(UserRegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new UserAlreadyExistsException(String.format(AppConstants.USER_EMAIL_ALREADY_EXISTS, request.getEmail()));
		}
		
		UserEntity user = new UserEntity();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		// Set default roles if needed
		if (request.getRoles() != null && !request.getRoles().isEmpty()) {
			// Ensure roles have ROLE_ prefix
			List<String> formattedRoles = request.getRoles().stream()
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase())
				.toList();
			user.setRoles(formattedRoles);
		} else {
			user.setRoles(List.of("ROLE_USER"));
		}
		
		return userRepository.save(user);
	}

	@Override
	public boolean userExists(String email) {
		return existsByEmail(email);
	}

	@Override
	public List<UserEntity> searchUsers(String email, String name) {
		return userRepository.findByEmailOrNameSafely(email, name);
	}
}
