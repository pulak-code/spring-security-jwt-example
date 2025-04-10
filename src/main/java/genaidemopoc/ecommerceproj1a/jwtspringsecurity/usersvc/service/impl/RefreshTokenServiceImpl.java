package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.TokenResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidRefreshTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.RefreshToken;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.RefreshTokenRepository;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.RefreshTokenService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;

/**
 * Implementation of RefreshTokenService interface
 * Manages refresh tokens stored in MongoDB
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private JWTUtil jwtUtil;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenDurationMs;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String userId, String userEmail, String tokenValue, Instant expiryDate) {
        log.debug("Creating refresh token for user: {}", userId);
        
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setUserEmail(userEmail);
        refreshToken.setToken(tokenValue);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiryDate(expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        log.debug("Finding refresh token: {}", token);
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public boolean verifyExpiration(String token) {
        log.debug("Verifying token expiration: {}", token);
        
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        
        if (!refreshToken.isPresent()) {
            log.debug("Token not found: {}", token);
            return false;
        }
        
        if (refreshToken.get().isExpired()) {
            log.debug("Token expired: {}", token);
            refreshTokenRepository.delete(refreshToken.get());
            return false;
        }
        
        return true;
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        log.debug("Deleting refresh token: {}", token);
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public long deleteByUserId(String userId) {
        log.debug("Deleting all refresh tokens for user ID: {}", userId);
        return refreshTokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public long deleteByUserEmail(String userEmail) {
        log.debug("Deleting all refresh tokens for user email: {}", userEmail);
        return refreshTokenRepository.deleteByUserEmail(userEmail);
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 86400000) // Run once a day
    public long removeExpiredTokens() {
        log.debug("Removing expired refresh tokens");
        return refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
    
    @Override
    @Transactional
    public TokenResponse refreshToken(String refreshToken) {
        log.debug("Refreshing token: {}", refreshToken);
        
        if (!verifyExpiration(refreshToken)) {
            throw new InvalidJWTTokenException("Refresh token is invalid or expired");
        }
        
        Optional<RefreshToken> tokenOpt = findByToken(refreshToken);
        if (!tokenOpt.isPresent()) {
            throw new InvalidJWTTokenException("Refresh token not found");
        }
        
        RefreshToken token = tokenOpt.get();
        String userEmail = token.getUserEmail();
        
        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(userEmail);
        
        // Generate new refresh token
        String newRefreshToken = jwtUtil.generateRefreshToken(userEmail);
        
        // Save new refresh token
        saveRefreshToken(newRefreshToken, token.getUserId(), userEmail);
        
        // Delete old refresh token
        deleteToken(refreshToken);
        
        return new TokenResponse(newAccessToken, newRefreshToken);
    }
    
    @Override
    @Transactional
    public void revokeRefreshToken(String token) {
        log.debug("Revoking refresh token: {}", token);
        deleteToken(token);
    }
    
    @Override
    @Transactional
    public void revokeAllUserTokens(String userId) {
        log.debug("Revoking all refresh tokens for user ID: {}", userId);
        deleteByUserId(userId);
    }
    
    @Override
    @Transactional
    public void saveRefreshToken(String token, String userId, String userEmail) {
        log.debug("Saving refresh token for user: {}", userEmail);
        Instant expiryDate = Instant.now().plusSeconds(jwtUtil.getRefreshTokenExpirationSeconds());
        createRefreshToken(userId, userEmail, token, expiryDate);
    }
} 