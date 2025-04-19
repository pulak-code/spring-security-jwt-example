package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    
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
     * User's username (typically the name)
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
     * Creates a successful authentication response with separate access and refresh tokens.
     * Used for Login/Refresh.
     *
     * @param accessToken JWT access token
     * @param refreshToken JWT refresh token
     * @return AuthResponse with token details
     */
    public static AuthResponse success(String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Authentication successful")
                .build();
    }
    
    /**
     * Creates a successful registration response with full user details and tokens.
     *
     * @param userId User's unique identifier
     * @param username User's username (or name)
     * @param email User's email address
     * @param roles User's assigned roles
     * @param accessToken The access token
     * @param refreshToken The refresh token (No longer needed in response body)
     * @return Complete AuthResponse for successful registration
     */
    public static AuthResponse registrationSuccess(String userId, String username, String email, 
                                                 List<String> roles, String accessToken, String refreshToken) { // refreshToken param still here but not used
        return AuthResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .roles(roles)
                .accessToken(accessToken)
                // .refreshToken(refreshToken) // Do not set refreshToken in the response body
                .message("Registration successful")
                .build();
    }
}
