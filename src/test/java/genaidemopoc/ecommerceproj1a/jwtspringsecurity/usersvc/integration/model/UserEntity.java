package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.model;

import java.util.List;

/**
 * Entity representing a user in the system.
 * Simplified version for integration testing.
 */
public class UserEntity {
    private String id;
    private String email;
    private String password;
    private String name;
    private List<String> roles;
    private boolean active;
    
    public UserEntity() {
    }
    
    public UserEntity(String id, String email, String password, String name, List<String> roles, boolean active) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
        this.active = active;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
} 