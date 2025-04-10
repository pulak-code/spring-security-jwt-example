package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for authentication response.
 * Contains JWT token information and user details to be returned to the client.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    /**
     * JWT token for authentication (legacy field for backward compatibility)
     */
    private String token;
    
    /**
     * JWT access token for authentication (new primary field)
     */
    private String accessToken;
    
    /**
     * JWT refresh token for getting new access tokens
     */
    private String refreshToken;
    
    /**
     * Token type, typically "Bearer"
     */
    @Builder.Default
    private String type = "Bearer";
    
    /**
     * Response message (success or error)
     */
    private String message;
    
    /**
     * User's unique identifier
     */
    private String userId;
    
    /**
     * User's username
     */
    private String username;
    
    /**
     * User's email address
     */
    private String email;
    
    /**
     * User's assigned roles
     */
    private List<String> roles;
    
    /**
     * TokenResponse containing access and refresh tokens
     */
    private TokenResponse tokens;
    
    /**
     * Creates an error response with the specified error message.
     *
     * @param errorMessage The error message
     * @return AuthResponse with only error message populated
     */
    public static AuthResponse error(String errorMessage) {
        return AuthResponse.builder()
                .message(errorMessage)
                .build();
    }
    
    /**
     * Creates a successful authentication response with just a token.
     *
     * @param token JWT access token
     * @return AuthResponse with token details
     */
    public static AuthResponse success(String token) {
        return AuthResponse.builder()
                .token(token)
                .accessToken(token)  // Set both for compatibility
                .message("Authentication successful")
                .build();
    }
    
    /**
     * Creates a successful authentication response with separate access and refresh tokens.
     *
     * @param accessToken JWT access token
     * @param refreshToken JWT refresh token
     * @return AuthResponse with both token details
     */
    public static AuthResponse success(String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .token(accessToken)  // For backward compatibility
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Authentication successful")
                .build();
    }
    
    /**
     * Creates a simple token response with the specified token.
     *
     * @param token The JWT token
     * @param type The token type (e.g., "Bearer")
     * @param message A success or error message
     * @return AuthResponse with token details
     */
    public static AuthResponse token(String token, String type, String message) {
        return AuthResponse.builder()
                .token(token)
                .accessToken(token)  // Set both for compatibility
                .type(type)
                .message(message)
                .build();
    }
    
    /**
     * Creates a successful authentication response with full user details.
     *
     * @param userId User's unique identifier
     * @param username User's username
     * @param email User's email address
     * @param roles User's assigned roles
     * @param tokens TokenResponse containing access and refresh tokens
     * @return Complete AuthResponse for successful authentication
     */
    public static AuthResponse success(String userId, String username, String email, 
                                      List<String> roles, TokenResponse tokens) {
        // Extract tokens from TokenResponse if available
        String accessTokenValue = tokens != null ? tokens.getAccessToken() : null;
        String refreshTokenValue = tokens != null ? tokens.getRefreshToken() : null;
        
        return AuthResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .roles(roles)
                .tokens(tokens)
                .token(accessTokenValue)        // For backward compatibility
                .accessToken(accessTokenValue)  // New field
                .refreshToken(refreshTokenValue) // New field
                .message("Authentication successful")
                .build();
    }
    
    /**
     * Gets the access token, maintaining compatibility with older code.
     * If accessToken is null but token is set, returns token.
     * 
     * @return The access token
     */
    public String getAccessToken() {
        return accessToken != null ? accessToken : token;
    }
    
    /**
     * Sets the access token and also updates the legacy token field for backward compatibility.
     * 
     * @param accessToken The access token to set
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        this.token = accessToken; // Keep legacy field in sync
    }
    
    /**
     * Sets the token and also updates the accessToken field for compatibility.
     * 
     * @param token The token to set
     */
    public void setToken(String token) {
        this.token = token;
        this.accessToken = token; // Keep new field in sync
    }
}
