package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto;

import java.util.List;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user self-edit requests.
 * Contains personal information fields that a user can update.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSelfEditRequest {
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
    
    // Allow users to update their addresses
    private List<Address> addresses;
}
