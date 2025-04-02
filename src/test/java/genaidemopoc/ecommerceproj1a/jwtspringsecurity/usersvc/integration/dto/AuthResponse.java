package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto;

import lombok.Data;

/**
 * Data Transfer Object for authentication responses.
 * Used specifically for integration testing.
 */
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String msg;
    private String userId;
    private String email;
    
    public AuthResponse() {
    }
    
    public AuthResponse(String accessToken, String refreshToken, String msg) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.msg = msg;
    }
    
    public AuthResponse(String accessToken, String refreshToken, String msg, String userId, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.msg = msg;
        this.userId = userId;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // Alias for msg for testing compatibility
    public String getMessage() {
        return msg;
    }
    
    public void setMessage(String message) {
        this.msg = message;
    }
} 