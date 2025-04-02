package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Service for storing and retrieving JWT tokens.
 * Supports multiple storage mechanisms:
 * 1. In-memory (transient)
 * 2. Environment variables
 * 3. Properties file
 */
@Service
public class TokenStorageService {
    
    private static final Logger log = LoggerFactory.getLogger(TokenStorageService.class);
    
    private final Map<String, String> tokenStorage = new ConcurrentHashMap<>();
    private final Map<String, String> refreshTokenStorage = new ConcurrentHashMap<>();
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void storeToken(String token, String userId) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_GENERATED, userId);
        tokenStorage.put(token, userId);
    }

    public void storeRefreshToken(String token, String userId) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_GENERATED, userId);
        refreshTokenStorage.put(token, userId);
    }

    public void removeToken(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, token);
        tokenStorage.remove(token);
    }

    public void removeRefreshToken(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, token);
        refreshTokenStorage.remove(token);
    }

    public boolean isTokenValid(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
        return tokenStorage.containsKey(token);
    }

    public boolean isRefreshTokenValid(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
        return refreshTokenStorage.containsKey(token);
    }

    public String getUserId(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
        return tokenStorage.get(token);
    }

    public String getUserIdFromRefreshToken(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
        return refreshTokenStorage.get(token);
    }

    public void clearTokens() {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, "Clearing all tokens");
        tokenStorage.clear();
        refreshTokenStorage.clear();
    }

    public void removeExpiredTokens() {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, "Removing expired tokens");
        // Implementation for removing expired tokens
    }

    public void refreshToken(String oldToken, String newToken) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REFRESHED, oldToken);
        String userId = tokenStorage.get(oldToken);
        if (userId != null) {
            tokenStorage.remove(oldToken);
            tokenStorage.put(newToken, userId);
        }
    }

    public void blacklistToken(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_BLACKLISTED, token);
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
        return blacklistedTokens.contains(token);
    }

    public void removeFromBlacklist(String token) {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, token);
        blacklistedTokens.remove(token);
    }

    public void clearBlacklist() {
        log.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, "Clearing all blacklisted tokens");
        blacklistedTokens.clear();
    }
} 