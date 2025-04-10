package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration.config;

import java.time.Duration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Simple test configuration for integration tests.
 */
@TestConfiguration
@Profile("test")
@EnableWebMvc
public class TestConfig {

    /**
     * Creates a RestTemplate with short timeouts for testing.
     */
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000); // 1 second timeout
        factory.setReadTimeout(1000);
        
        return new RestTemplateBuilder()
                .requestFactory(() -> factory)
                .connectTimeout(Duration.ofSeconds(1))
                .readTimeout(Duration.ofSeconds(1))
                .build();
    }
} 