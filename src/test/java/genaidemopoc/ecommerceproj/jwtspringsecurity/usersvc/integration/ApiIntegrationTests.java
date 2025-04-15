package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.integration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.TestConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.RefreshTokenRepository;

/**
 * Basic integration tests for the User Service API.
 * Tests focus on repository operations and data integrity.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setup() {
        // Clean up any existing test data
        userRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        // Clean up test data
        userRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    /**
     * Test user registration endpoint
     */
    @Test
    public void testUserRegistration() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("test" + System.currentTimeMillis() + "@example.com");
        request.setPassword(TestConstants.TEST_USER_PASSWORD);
        request.setName(TestConstants.TEST_USER_NAME);
        request.setUsername(request.getEmail().split("@")[0]);

        // Register user
        mockMvc.perform(post(TestConstants.API_AUTH_USER_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Verify user exists in database
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
        assertTrue(userOpt.isPresent(), "User should exist in database");
        assertEquals(request.getName(), userOpt.get().getName(), "Name should match");
        assertTrue(userOpt.get().getRoles().contains("ROLE_USER"), "User should have user role");
    }

    /**
     * Test admin user registration
     */
    @Test
    public void testAdminRegistration() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail("admin" + System.currentTimeMillis() + "@example.com");
        request.setPassword(TestConstants.TEST_ADMIN_PASSWORD);
        request.setName(TestConstants.TEST_ADMIN_NAME);
        request.setUsername(request.getEmail().split("@")[0]);
        request.setRoles(List.of(TestConstants.TEST_ADMIN_ROLE));

        // Register admin
        mockMvc.perform(post(TestConstants.API_AUTH_USER_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Verify admin exists in database
        Optional<UserEntity> userOpt = userRepository.findByEmail(request.getEmail());
        assertTrue(userOpt.isPresent(), "Admin should exist in database");
        assertEquals(request.getName(), userOpt.get().getName(), "Name should match");
        assertTrue(userOpt.get().getRoles().contains(TestConstants.TEST_ADMIN_ROLE), "Admin should have admin role");
    }
} 