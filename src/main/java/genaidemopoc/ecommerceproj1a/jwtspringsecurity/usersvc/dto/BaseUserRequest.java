package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Base class for user-related requests.
 * Contains common fields like email and password with their validations.
 */
@Getter
@Setter
@SuperBuilder
public abstract class BaseUserRequest {
    
    @NotBlank(message = ValidationConstants.EMAIL_REQUIRED)
    @Email(message = ValidationConstants.EMAIL_INVALID)
    @Size(max = ValidationConstants.MAX_EMAIL_LENGTH, message = ValidationConstants.EMAIL_TOO_LONG)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_REQUIRED)
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH, message = ValidationConstants.PASSWORD_REQUIREMENTS)
    @Pattern(regexp = ValidationConstants.PASSWORD_PATTERN, message = ValidationConstants.PASSWORD_INVALID)
    private String password;

    @NotBlank(message = ValidationConstants.USERNAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_USERNAME_LENGTH, max = ValidationConstants.MAX_USERNAME_LENGTH, message = ValidationConstants.USERNAME_FORMAT)
    @Pattern(regexp = ValidationConstants.USERNAME_PATTERN, message = ValidationConstants.USERNAME_INVALID)
    private String username;

    protected BaseUserRequest() {
    }

    protected BaseUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
