package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.FailedLoginAttempt;

@Repository
public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {
    
    Optional<FailedLoginAttempt> findByEmail(String email);
    
    @Query("SELECT f FROM FailedLoginAttempt f WHERE f.isLocked = true AND f.lockoutEndTime < ?1")
    List<FailedLoginAttempt> findExpiredLockouts(LocalDateTime now);
    
    @Modifying
    @Query("UPDATE FailedLoginAttempt f SET f.isLocked = false, f.attempts = 0, f.lockoutEndTime = null, f.updatedAt = ?2 WHERE f.email = ?1")
    void resetAttempts(String email, LocalDateTime now);
    
    @Query("SELECT COUNT(f) > 0 FROM FailedLoginAttempt f WHERE f.email = ?1 AND f.isLocked = true AND f.lockoutEndTime > ?2")
    boolean isAccountLocked(String email, LocalDateTime now);
}
