package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Simple test to verify that our REST endpoints are properly configured
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VerifyEndpointsConfigured {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void verifyStatusEndpoint() {
        // Test the status endpoint
        String url = "http://localhost:" + port + "/rest-test/status";
        System.out.println("Testing endpoint: " + url);
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        System.out.println("Response: " + response.getStatusCode() + " - " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
} 