package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserSelfEditRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
	UserEntity getUserById(String id);
	UserEntity getUserByEmail(String email);
	List<UserEntity> getAllUsers();
	UserEntity saveUser(UserEntity user);
	UserEntity saveUser(UserRegisterRequest request);
	void deleteUser(String id);
	UserEntity updateUser(String id, UserUpdateRequest request);
	UserEntity editUserDetails(String id, UserSelfEditRequest request);
	boolean existsByEmail(String email);
	boolean userExists(String email);
	List<UserEntity> searchUsers(String email, String name);
}
