package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Test configuration to provide necessary beans for testing
 */
@TestConfiguration
public class TestConfig {
    
    /**
     * Provides a RestTemplate for tests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 