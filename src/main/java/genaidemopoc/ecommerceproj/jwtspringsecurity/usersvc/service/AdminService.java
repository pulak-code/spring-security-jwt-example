package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service;

import java.security.Principal;
import java.util.List;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface AdminService {

	Logger log = LoggerFactory.getLogger(AdminService.class);

	List<UserEntity> getAllUsers();

	List<UserEntity> searchUser(String email, String username);

	void deleteAllUsers(Principal principal);

	UserEntity updateUser(String userId, @Valid UserUpdateRequest request, Principal principal);

	default void assignDefaultRole(UserEntity user) {
		if (user.getRoles() == null || user.getRoles().isEmpty()) {
			log.warn("User found but has no roles: {}", user.getEmail());
			// Assign default role to prevent authentication issues
			log.debug("Assigning default ROLE_USER for: {}", user.getEmail());
			user.setRoles(List.of("ROLE_USER"));
			// Save the updated user
			// Assuming userRepository is available to save the user
		}
	}

}
