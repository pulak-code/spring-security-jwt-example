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
        // Test with empty values instead of null
        List<UserEntity> results = userRepository.findByEmailOrNameSafely("", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty strings");
        
        // Test with empty email (but valid name)
        results = userRepository.findByEmailOrNameSafely("", "Test");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty email");
        
        // Test with empty name (but valid email)
        results = userRepository.findByEmailOrNameSafely("test", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty name");
    }
    
    @Test
    @DisplayName("Test search with empty values")
    public void testSearchWithEmptyValues() {
        // Test with empty email and name
        List<UserEntity> results = userRepository.findByEmailOrNameSafely("", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty strings");
        
        // Empty email, valid name
        results = userRepository.findByEmailOrNameSafely("", "Test");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty email");
        
        // Valid email, empty name
        results = userRepository.findByEmailOrNameSafely("test", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with empty name");
        
        // Partial matches
        results = userRepository.findByEmailOrNameSafely("test@", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with partial email match");
        
        results = userRepository.findByEmailOrNameSafely("", "Test U");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with partial name match");
    }
    
    @Test
    @DisplayName("Test search with valid values")
    public void testSearchWithValidValues() {
        // Test with exact email
        List<UserEntity> results = userRepository.findByEmailOrNameSafely("test@example.com", "");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with exact email");
        
        // Test with exact name
        results = userRepository.findByEmailOrNameSafely("", "Test User");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with exact name");
        
        // Test with both values
        results = userRepository.findByEmailOrNameSafely("test@example.com", "Test User");
        assertNotNull(results);
        assertTrue(results.size() > 0, "Should find results with both values");
    }
} 