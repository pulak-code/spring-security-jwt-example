package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.ValidationConstants;

/**
 * Data Transfer Object for user registration requests.
 * Extends BaseUserRequest to inherit email and password fields.
 */
@Getter
@Setter
@SuperBuilder
public class UserRegisterRequest extends BaseUserRequest {
    
    @NotBlank(message = ValidationConstants.NAME_REQUIRED)
    @Size(min = ValidationConstants.MIN_NAME_LENGTH, max = ValidationConstants.MAX_NAME_LENGTH, message = ValidationConstants.NAME_LENGTH)
    @Pattern(regexp = ValidationConstants.NAME_PATTERN, message = ValidationConstants.NAME_INVALID)
    private String name;

    private String address;

    private List<String> roles;

    public UserRegisterRequest() {
        super();
    }

    public UserRegisterRequest(String email, String password, String name, String address, List<String> roles) {
        super(email, password);
        this.name = name;
        this.address = address;
        this.roles = roles;
    }
}
