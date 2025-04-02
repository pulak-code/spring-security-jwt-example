package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String>, CustomUserRepository {

	boolean existsByEmail(String email);
	Optional<UserEntity> findByEmail(String email);
	@Query("{ $or: [ " +
		   "  { email: { $regex: ?0, $options: 'i' } }, " +
		   "  { name: { $regex: ?1, $options: 'i' } } " +
		   "] }")
	List<UserEntity> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name);
	
	@Query(value = "{ 'email': { $ne: ?0 } }", delete = true)
	void deleteByEmailNot(String email);
}

