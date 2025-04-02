package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserSelfEditRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserUpdateRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

public interface UserService {

	UserEntity getUserById(String userId);

	void deleteUser(String userId);

	UserEntity editUserDetails(String userId, UserSelfEditRequest request);

	UserEntity updateUser(String userId, UserUpdateRequest request);

	UserEntity getUserByEmail(String email);

	UserEntity saveUser(UserEntity user);

	boolean userExists(String email);

	UserEntity saveUser(UserRegisterRequest registerRequest);
}
