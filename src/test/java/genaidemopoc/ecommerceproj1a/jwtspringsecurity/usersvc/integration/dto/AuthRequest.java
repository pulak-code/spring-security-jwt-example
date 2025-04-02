package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto;

import lombok.Data;

/**
 * Data Transfer Object for authentication requests.
 * Used specifically for integration testing.
 */
@Data
public class AuthRequest {
    private String email;
    private String password;
    
    public AuthRequest() {
    }
    
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
} 