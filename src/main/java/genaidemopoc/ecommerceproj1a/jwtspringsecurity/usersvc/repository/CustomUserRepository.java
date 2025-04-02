package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.util.List;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

/**
 * Custom repository interface to add methods that handle null parameters more robustly.
 */
public interface CustomUserRepository {
    
    /**
     * Find users by email or name, handling null/empty values more robustly.
     * 
     * @param email The email to search for (can be null or empty)
     * @param name The name to search for (can be null or empty)
     * @return A list of matching users
     */
    List<UserEntity> findByEmailOrNameSafely(String email, String name);
} 