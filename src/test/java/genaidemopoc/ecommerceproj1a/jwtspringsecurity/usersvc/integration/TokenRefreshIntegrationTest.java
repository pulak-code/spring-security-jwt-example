package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceTestUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;

/**
 * Simple integration test for basic token functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TokenRefreshIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private static String accessToken;
    
    /**
     * Setup test data before running tests.
     */
    @BeforeAll
    public static void setup() {
        // No longer needed as the email is not used
        // String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        // testEmail = "test-" + uniqueId + "@example.com";
    }
    
    @BeforeEach
    void setupMockMvc() {
        accessToken = null; // Reset token for each test to ensure clean state
    }
    
    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
    
    /**
     * Test user registration using TestRestTemplate
     */
    @Test
    @Order(1)
    void testRegisterUser() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email(UserServiceTestUserServiceConstants.TEST_USER_EMAIL)
                .password(UserServiceTestUserServiceConstants.TEST_USER_PASSWORD)
                .name(UserServiceTestUserServiceConstants.TEST_USER_NAME)
                .build();
                
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegisterRequest> entity = new HttpEntity<>(request, headers);
        
        // Try register endpoints in order of preference
        String[] endpoints = {
            "/rest-test/auth/register"
        };
        
        ResponseEntity<String> response = null;
        
        for (String endpoint : endpoints) {
            try {
                System.out.println("Trying registration at: " + endpoint);
                response = restTemplate.exchange(
                        getBaseUrl() + endpoint,
                        HttpMethod.POST,
                        entity,
                        String.class);
                
                System.out.println("Registration status: " + response.getStatusCode());
                System.out.println("Registration response: " + response.getBody());
                
                if (response.getStatusCode().is2xxSuccessful()) {
                    break;
                }
            } catch (RestClientException e) {
                System.err.println("Error with endpoint " + endpoint + ": " + e.getMessage());
            }
        }
        
        assertNotNull(response, "Should have received a response from at least one endpoint");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    
    /**
     * Test login to obtain an access token
     */
    @Test
    @Order(2)
    void testLogin() throws Exception {
        UserLoginRequest request = UserLoginRequest.builder()
                .email(UserServiceTestUserServiceConstants.TEST_USER_EMAIL)
                .password(UserServiceTestUserServiceConstants.TEST_USER_PASSWORD)
                .build();
                
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserLoginRequest> entity = new HttpEntity<>(request, headers);
        
        // Try login endpoints in order of preference
        String[] endpoints = {
            "/rest-test/auth/login"
        };
        
        ResponseEntity<String> response = null;
        
        for (String endpoint : endpoints) {
            try {
                System.out.println("Trying login at: " + endpoint);
                response = restTemplate.exchange(
                        getBaseUrl() + endpoint,
                        HttpMethod.POST,
                        entity,
                        String.class);
                
                System.out.println("Login status: " + response.getStatusCode());
                System.out.println("Login response: " + response.getBody());
                System.out.println("Headers: " + response.getHeaders());
                
                if (response.getStatusCode().is2xxSuccessful()) {
                    break;
                }
            } catch (RestClientException e) {
                System.err.println("Error with endpoint " + endpoint + ": " + e.getMessage());
            }
        }
        
        assertNotNull(response, "Should have received a response from at least one endpoint");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().getFirst("Authorization"));
        accessToken = response.getHeaders().getFirst("Authorization");
    }
    
    /**
     * Test token usage for authentication
     */
    @Test
    @Order(3)
    void testTokenAuthentication() throws Exception {
        // Only run if we have an access token from previous test
        if (accessToken == null) {
            testLogin(); // Get token first
        }
        
        // Should now have a token
        assertNotNull(accessToken, "Should have a valid access token");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<String> response = null;
        
        try {
            response = restTemplate.exchange(
                    getBaseUrl() + "/rest-test/user/profile",
                    HttpMethod.GET,
                    entity,
                    String.class);
            
            System.out.println("Profile status: " + response.getStatusCode());
            System.out.println("Profile response: " + response.getBody());
        } catch (RestClientException e) {
            System.err.println("Error accessing profile: " + e.getMessage());
        }
        
        assertNotNull(response, "Should have received a response from profile endpoint");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test the basic status endpoint to ensure controller is accessible
     */
    @Test
    @Order(0)
    void testStatusEndpoint() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/rest-test/status", 
                String.class);
        
        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("RestTestController is working", response.getBody());
    }
} 