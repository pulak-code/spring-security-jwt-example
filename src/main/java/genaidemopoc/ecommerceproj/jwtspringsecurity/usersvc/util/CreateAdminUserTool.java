package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.util;

import java.util.Arrays;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;

@Component
@Profile("!test")
public class CreateAdminUserTool implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateAdminUserTool(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if admin user already exists to avoid duplication
        if (!userRepository.existsByEmail("admin@example.com")) {
            System.out.println("Creating admin user...");
            
            UserEntity adminUser = new UserEntity();
            adminUser.setName("Admin User");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("Password@1234"));
            adminUser.setRoles(new ArrayList<>(Arrays.asList("ROLE_ADMIN")));
            
            userRepository.save(adminUser);
            System.out.println("Admin user created successfully with email: admin@example.com and password: Password@1234");
            System.out.println("Admin roles: " + adminUser.getRoles());
        } else {
            System.out.println("Admin user already exists");
            
            // Update existing admin user to ensure correct password and roles
            UserEntity existingAdmin = userRepository.findByEmail("admin@example.com").orElse(null);
            if (existingAdmin != null) {
                System.out.println("Existing admin user found with roles: " + existingAdmin.getRoles());
                
                // Always update password to ensure it matches
                String rawPassword = "Password@1234";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                existingAdmin.setPassword(encodedPassword);
                
                // Ensure roles include ROLE_ADMIN
                if (existingAdmin.getRoles() == null) {
                    existingAdmin.setRoles(new ArrayList<>());
                }
                if (!existingAdmin.getRoles().contains("ROLE_ADMIN")) {
                    existingAdmin.getRoles().add("ROLE_ADMIN");
                }
                
                userRepository.save(existingAdmin);
                System.out.println("Updated existing admin user credentials");
                System.out.println("Updated admin password hash: " + encodedPassword);
                System.out.println("Updated admin roles: " + existingAdmin.getRoles());
                
                // Test password match
                boolean passwordMatches = passwordEncoder.matches(rawPassword, existingAdmin.getPassword());
                System.out.println("Password match test: " + passwordMatches);
            }
        }
    }
} 