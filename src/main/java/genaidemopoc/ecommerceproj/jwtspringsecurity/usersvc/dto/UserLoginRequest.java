package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.ValidationConstants;

/**
 * Data Transfer Object for user login requests.
 * Extends BaseUserRequest to inherit email and password fields.
 * NOTE: Username is currently not used for login, prefer LoginRequest DTO.
 */
@Getter
@Setter
@SuperBuilder
public class UserLoginRequest extends BaseUserRequest {
    
    // Removed username field and validation as it's not used for login
    // @NotBlank(message = MessageUserServiceConstants.USERNAME_IS_REQUIRED)
    // @Pattern(regexp = "^[a-zA-Z0-9_-]{3,50}$", 
    //          message = MessageUserServiceConstants.USERNAME_FORMAT)
    // private String username;

    @NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
    @Email(message = ValidationConstants.EMAIL_INVALID)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    private String password;

    public UserLoginRequest() {
        super();
    }

    // Updated constructor
    public UserLoginRequest(String email, String password) {
        super(email, password);
    }
}
