package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.RefreshToken;

/**
 * Repository interface for RefreshToken document operations
 */
@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    
    /**
     * Find a refresh token by its token value
     * @param token The token string
     * @return Optional RefreshToken
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Find all refresh tokens for a specific user
     * @param userId The user ID
     * @return List of RefreshToken
     */
    List<RefreshToken> findByUserId(String userId);
    
    /**
     * Find all refresh tokens for a specific user email
     * @param userEmail The user email
     * @return List of RefreshToken
     */
    List<RefreshToken> findByUserEmail(String userEmail);
    
    /**
     * Delete a refresh token by its token value
     * @param token The token string
     * @return Number of deleted tokens
     */
    long deleteByToken(String token);
    
    /**
     * Delete all refresh tokens for a specific user
     * @param userId The user ID
     * @return Number of deleted tokens
     */
    long deleteByUserId(String userId);
    
    /**
     * Delete all refresh tokens for a specific user email
     * @param userEmail The user email
     * @return Number of deleted tokens
     */
    long deleteByUserEmail(String userEmail);
    
    /**
     * Delete all expired tokens
     * @param now Current time
     * @return Number of deleted tokens
     */
    long deleteByExpiryDateBefore(Instant now);
    
} 