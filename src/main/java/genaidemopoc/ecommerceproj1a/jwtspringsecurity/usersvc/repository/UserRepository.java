package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

	boolean existsByEmail(String email);
	
	Optional<UserEntity> findByEmail(String email);
	
	/**
	 * Find users by email or name, handling null/empty values more robustly.
	 * This method replaces the custom repository implementation.
	 * 
	 * @param email The email to search for (can be null or empty)
	 * @param name The name to search for (can be null or empty)
	 * @return A list of matching users
	 */
	@Query(value = "{ $or: [ " +
		   "  { email: { $regex: ?0, $options: 'i' } }, " +
		   "  { name: { $regex: ?1, $options: 'i' } } " +
		   "] }")
	List<UserEntity> findByEmailOrNameSafely(String email, String name);
	
	@Query(value = "{ 'email': { $ne: ?0 } }", delete = true)
	void deleteByEmailNot(String email);
}

