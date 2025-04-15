package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

/**
 * Service responsible for recording authentication metrics
 */
@Service
public class AuthenticationMetricsService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationMetricsService.class);
    
    private final MeterRegistry meterRegistry;
    
    private Counter authSuccessCounter;
    private Counter authFailureCounter;
    
    public AuthenticationMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @PostConstruct
    public void initMetrics() {
        authSuccessCounter = Counter.builder("security.authentication")
            .description("Number of successful authentication attempts")
            .tag("result", "success")
            .register(meterRegistry);
            
        authFailureCounter = Counter.builder("security.authentication")
            .description("Number of failed authentication attempts")
            .tag("result", "failure")
            .register(meterRegistry);
            
        logger.info("Authentication metrics initialized");
    }
    
    /**
     * Record successful authentication
     */
    public void recordAuthenticationSuccess() {
        authSuccessCounter.increment();
        logger.debug("Authentication success recorded");
    }
    
    /**
     * Record failed authentication
     */
    public void recordAuthenticationFailure() {
        authFailureCounter.increment();
        logger.debug("Authentication failure recorded");
    }
}

