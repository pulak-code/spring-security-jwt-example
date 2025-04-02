package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceTestUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.constants.UserServiceTestConstants;

/**
 * Basic integration tests for the User Service API.
 * Tests cover the core functionality: registration, authentication, and authorization.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ApiIntegrationTests extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String userToken;
    private static String adminToken;

    @BeforeAll
    public static void setupEmails() {
        // No longer needed as we're using constants directly
        // String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        // testUserEmail = UserServiceTestConstants.USER_EMAIL_PREFIX + uniqueId + UserServiceTestConstants.EMAIL_DOMAIN;
        // testAdminEmail = UserServiceTestConstants.ADMIN_EMAIL_PREFIX + uniqueId + UserServiceTestConstants.EMAIL_DOMAIN;
    }

    /**
     * Test user registration
     */
    @Test
    @Order(1)
    public void testUserRegistration() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail(UserServiceTestUserServiceConstants.TEST_USER_EMAIL);
        request.setPassword(UserServiceTestUserServiceConstants.TEST_USER_PASSWORD);
        request.setName(UserServiceTestUserServiceConstants.TEST_USER_NAME);

        MvcResult result = mockMvc.perform(post("/rest-test/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    /**
     * Test admin user registration
     */
    @Test
    @Order(2)
    public void testAdminRegistration() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail(UserServiceTestUserServiceConstants.TEST_ADMIN_EMAIL);
        request.setPassword(UserServiceTestUserServiceConstants.TEST_ADMIN_PASSWORD);
        request.setName(UserServiceTestUserServiceConstants.TEST_ADMIN_NAME);
        request.setRoles(List.of(UserServiceTestUserServiceConstants.TEST_ADMIN_ROLE));

        MvcResult result = mockMvc.perform(post("/rest-test/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
    }

    /**
     * Test user login
     */
    @Test
    @Order(3)
    public void testUserLogin() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(UserServiceTestUserServiceConstants.TEST_USER_EMAIL);
        request.setPassword(UserServiceTestUserServiceConstants.TEST_USER_PASSWORD);

        MvcResult result = mockMvc.perform(post("/rest-test/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    /**
     * Test admin login
     */
    @Test
    @Order(4)
    public void testAdminLogin() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(UserServiceTestUserServiceConstants.TEST_ADMIN_EMAIL);
        request.setPassword(UserServiceTestUserServiceConstants.TEST_ADMIN_PASSWORD);

        MvcResult result = mockMvc.perform(post("/rest-test/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    /**
     * Test authorization - access control for admin endpoints
     */
    @Test
    @Order(5)
    public void testAccessControl() {
        // Skip if user token is not available
        if (userToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // User tries to access admin endpoint
        ResponseEntity<Object> response = restTemplate.exchange(
                getBaseUrl() + UserServiceTestConstants.API_ADMIN_USERS,
                HttpMethod.GET,
                request,
                Object.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), 
                "Regular user should be forbidden from accessing admin endpoints");
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    /**
     * Test admin access to protected endpoints
     */
    @Test
    @Order(6)
    public void testAdminAccess() {
        // Skip if admin token is not available
        if (adminToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // Admin accesses admin endpoint
        ResponseEntity<Object> response = restTemplate.exchange(
                getBaseUrl() + UserServiceTestConstants.API_ADMIN_USERS,
                HttpMethod.GET,
                request,
                Object.class);

        assertTrue(response.getStatusCode().is2xxSuccessful(), 
                "Admin should be able to access admin endpoints");
        assertNotNull(response.getBody(), "Response body should not be null");
    }
} 