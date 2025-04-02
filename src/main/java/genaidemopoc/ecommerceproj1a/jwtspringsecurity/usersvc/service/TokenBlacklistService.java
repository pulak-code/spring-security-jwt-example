package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service;

public interface TokenBlacklistService {
    void blacklistToken(String token, Long expiryTime);
    boolean isTokenBlacklisted(String token);
    void removeFromBlacklist(String token);
} 