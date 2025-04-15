package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service;

import java.time.Instant;
import java.util.Optional;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.RefreshToken;

/**
 * Service interface for managing refresh tokens
 */
public interface RefreshTokenService {
    
    /**
     * Create and save a new refresh token
     * 
     * @param userId User ID
     * @param userEmail User email
     * @param tokenValue Token string
     * @param expiryDate Token expiration date
     * @return The created RefreshToken
     */
    RefreshToken createRefreshToken(String userId, String userEmail, String tokenValue, Instant expiryDate);
    
    /**
     * Find a refresh token by its token value
     * 
     * @param token Token string
     * @return Optional containing the token if found
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Verify if a token is valid (exists and not expired)
     * 
     * @param token Token string
     * @return true if valid, false otherwise
     */
    boolean verifyExpiration(String token);
    
    /**
     * Delete a token by its token value
     * 
     * @param token Token string
     */
    void deleteToken(String token);
    
    /**
     * Delete all tokens for a specific user
     * 
     * @param userId User ID
     * @return Number of tokens deleted
     */
    long deleteByUserId(String userId);
    
    /**
     * Delete all tokens for a specific user email
     * 
     * @param userEmail User email
     * @return Number of tokens deleted
     */
    long deleteByUserEmail(String userEmail);
    
    /**
     * Removes all expired tokens from the database
     * 
     * @return Number of tokens deleted
     */
    long removeExpiredTokens();

    TokenResponse refreshToken(String refreshToken);
    void revokeRefreshToken(String token);
    void revokeAllUserTokens(String userId);
    void saveRefreshToken(String token, String userId, String userEmail);
} 