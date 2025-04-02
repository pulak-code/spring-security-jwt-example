package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.dto;

import java.util.List;
import lombok.Data;

/**
 * Data Transfer Object for user registration.
 * Used specifically for integration testing.
 */
@Data
public class UserRegistrationDto {
    private String email;
    private String password;
    private String name;
    private String address;
    private List<String> roles;
    
    public UserRegistrationDto() {
    }
    
    public UserRegistrationDto(String email, String password, String name, List<String> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
} 