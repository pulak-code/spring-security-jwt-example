package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

import java.security.Principal;
import java.util.List;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import jakarta.validation.Valid;

public interface AdminService {

	List<UserEntity> getAllUsers();

	List<UserEntity> searchUser(String email, String username);

	void deleteAllUsers(Principal principal);

	UserEntity updateUser(String userId, @Valid UserUpdateRequest request, Principal principal);

}
