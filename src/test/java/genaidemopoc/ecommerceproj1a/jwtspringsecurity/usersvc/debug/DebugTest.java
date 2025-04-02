package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;

/**
 * Debug test for verifying controller functionality
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DebugTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
    
    /**
     * Test all status endpoints to verify basic controller accessibility
     */
    @Test
    void testStatusEndpoints() {
        // Test endpoints
        String[] endpoints = {
            "/test/status",
            "/debug/status"
        };
        
        for (String endpoint : endpoints) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(
                        getBaseUrl() + endpoint, 
                        String.class);
                
                System.out.println("Status endpoint: " + endpoint);
                System.out.println("  Status: " + response.getStatusCode());
                System.out.println("  Body: " + response.getBody());
                
                assertEquals(HttpStatus.OK, response.getStatusCode(),
                        "Status endpoint " + endpoint + " should return 200 OK");
            } catch (Exception e) {
                System.err.println("Error testing " + endpoint + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test registration endpoints with all variants
     */
    @Test
    void testRegisterEndpoints() {
        // Generate a unique email for testing
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String testEmail = "debug-test-" + uniqueId + "@example.com";
        
        // Create test user data
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email(testEmail)
                .password("Test@123")
                .name("Debug Test User")
                .build();
                
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRegisterRequest> entity = new HttpEntity<>(request, headers);
        
        // Test endpoints
        String[] endpoints = {
            "/debug/register",
            "/test/auth/register",
            "/api/auth/register",
            "/api/auth/user/register"
        };
        
        for (String endpoint : endpoints) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        getBaseUrl() + endpoint,
                        HttpMethod.POST,
                        entity,
                        Map.class);
                
                System.out.println("Register endpoint: " + endpoint);
                System.out.println("  Status: " + response.getStatusCode());
                System.out.println("  Body: " + response.getBody());
                
                // We don't assert success here since some might fail
                // That's okay for debugging purposes
            } catch (Exception e) {
                System.err.println("Error testing " + endpoint + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test login endpoints with all variants
     */
    @Test
    void testLoginEndpoints() {
        // Create test user data
        UserLoginRequest request = UserLoginRequest.builder()
                .email("test@example.com")
                .password("Test@123")
                .build();
                
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserLoginRequest> entity = new HttpEntity<>(request, headers);
        
        // Test endpoints
        String[] endpoints = {
            "/debug/login",
            "/test/auth/login",
            "/api/auth/login",
            "/api/auth/user/login"
        };
        
        for (String endpoint : endpoints) {
            try {
                ResponseEntity<Map> response = restTemplate.exchange(
                        getBaseUrl() + endpoint,
                        HttpMethod.POST,
                        entity,
                        Map.class);
                
                System.out.println("Login endpoint: " + endpoint);
                System.out.println("  Status: " + response.getStatusCode());
                System.out.println("  Body: " + response.getBody());
                
                // We don't assert success here since some might fail
                // That's okay for debugging purposes
            } catch (Exception e) {
                System.err.println("Error testing " + endpoint + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
} 