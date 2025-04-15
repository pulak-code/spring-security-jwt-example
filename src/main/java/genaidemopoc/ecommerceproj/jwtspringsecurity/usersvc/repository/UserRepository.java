package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

	boolean existsByEmail(String email);
	
	Optional<UserEntity> findByEmail(String email);
	
	void deleteByEmail(String email);
	
	/**
	 * Find users by email or name pattern matching.
	 * This basic query is used by the findByEmailOrNameSafely method.
	 * 
	 * @param email The email pattern to search for
	 * @param name The name pattern to search for
	 * @return A list of matching users
	 */
	@Query(value = "{ $or: [ " +
            "{ 'email': { $regex: ?0, $options: 'i' } }, " +
            "{ 'name': { $regex: ?1, $options: 'i' } } " +
            "] }", 
		fields = "{ 'password': 0 }")
	List<UserEntity> findByEmailOrNamePatterns(String emailPattern, String namePattern);
	
	/**
	 * Find all users in the system.
	 * Used as a fallback when no search criteria are provided.
	 * 
	 * @return A list of all users
	 */
	@Query(value = "{}", fields = "{ 'password': 0 }")
	List<UserEntity> findAllUsers();
	
	/**
	 * Find users by email or name, handling null/empty values safely.
	 * This method returns all users when both parameters are null or empty.
	 * 
	 * @param email The email to search for (can be null or empty)
	 * @param name The name to search for (can be null or empty)
	 * @return A list of matching users
	 */
	default List<UserEntity> findByEmailOrNameSafely(String email, String name) {
		boolean emailEmpty = (email == null || email.trim().isEmpty());
		boolean nameEmpty = (name == null || name.trim().isEmpty());
		
		// If both parameters are empty, return all users
		if (emailEmpty && nameEmpty) {
			return findAllUsers();
		}
		
		// Prepare search patterns, using "." as default for empty values 
		// which will match any character in regex
		String emailPattern = emailEmpty ? "." : email;
		String namePattern = nameEmpty ? "." : name;
		
		return findByEmailOrNamePatterns(emailPattern, namePattern);
	}
	
	@Query(value = "{ 'email': { $ne: ?0 } }", delete = true)
	void deleteByEmailNot(String email);
}

