package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;

/**
 * Tests for the UserRepository specifically focusing on the search functionality
 * that was causing issues with null values.
 */
@DataMongoTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    private UserEntity testUser;
    
    @BeforeEach
    public void setup() {
        // Create a test user
        testUser = new UserEntity();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRoles(List.of("ROLE_USER"));
        
        // Save to the DB
        userRepository.save(testUser);
    }
    
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("Test search with null values")
    public void testSearchWithNullValues() {
        // Test with null username (but valid email)
        List<UserEntity> results = userRepository.findByEmailOrNameSafely("test", null);
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with null name");
        
        // Test with null email (but valid username)
        results = userRepository.findByEmailOrNameSafely(null, "Test");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with null email");
        
        // Test with empty strings
        results = userRepository.findByEmailOrNameSafely("", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty strings");
    }
    
    @Test
    @DisplayName("Test search with empty values")
    public void testSearchWithEmptyValues() {
        // Empty email, valid name
        List<UserEntity> results = userRepository.findByEmailOrNameSafely("", "Test");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty email");
        
        // Valid email, empty name
        results = userRepository.findByEmailOrNameSafely("test", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty name");
    }
} 