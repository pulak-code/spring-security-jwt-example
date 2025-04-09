package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity class representing a refresh token stored in MongoDB.
 * Uses TTL index on expiryDate for automatic document expiration.
 */
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;
    
    @Indexed(unique = true)
    private String token;
    
    @Indexed
    private String userId;
    
    private String userEmail;
    
    private Instant createdAt;
    
    @Indexed(expireAfter = "0s")
    private Instant expiryDate;

    // Default constructor
    public RefreshToken() {
    }

    // Constructor with parameters
    public RefreshToken(String token, String userId, String userEmail, Instant expiryDate) {
        this.token = token;
        this.userId = userId;
        this.userEmail = userEmail;
        this.createdAt = Instant.now();
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }

    private boolean used = false;
    
    public boolean isUsed() {
        return used;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }
}