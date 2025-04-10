package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * Base test configuration providing common beans for tests
 */
@TestConfiguration
public class BaseTestConfig {

    /**
     * Provides a test-specific password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4); // Lower strength for faster tests
    }
    
    /**
     * Provides a simple meter registry for tests
     */
    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
}
