package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.FailedLoginAttempt;

@Repository
public interface FailedLoginAttemptRepository extends MongoRepository<FailedLoginAttempt, String> {
    
    Optional<FailedLoginAttempt> findByEmail(String email);
    
    @Query("{ 'isLocked': true, 'lockoutEndTime': { $lt: ?0 } }")
    List<FailedLoginAttempt> findExpiredLockouts(LocalDateTime now);
    
    // Note: MongoDB doesn't support @Modifying. This will need to be implemented in the service layer
    // using MongoTemplate or a direct update method
    void deleteByEmail(String email);
    
    @Query("{ 'email': ?0, 'isLocked': true, 'lockoutEndTime': { $gt: ?1 } }")
    Optional<FailedLoginAttempt> findLockedAccount(String email, LocalDateTime now);
    
    // Helper method to replace the isAccountLocked query
    default boolean isAccountLocked(String email, LocalDateTime now) {
        return findLockedAccount(email, now).isPresent();
    }
    
    /**
     * Resets the failed login attempts counter for the specified email.
     * This is typically called after a successful login.
     *
     * @param email the email address of the user
     * @param now the current timestamp
     */
    @Query(value = "{ 'email': ?0 }", fields = "{ 'attempts': 0, 'isLocked': false, 'updatedAt': ?1 }")
    void resetAttempts(String email, LocalDateTime now);
}
