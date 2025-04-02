package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Base class for integration tests.
 * Provides common setup and teardown methods for all integration tests.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;
    
    @Autowired(required = false)
    protected MongoTemplate mongoTemplate;
    
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
    
    /**
     * Setup logic before each test.
     * Clears any shared state to ensure test isolation.
     */
    @BeforeEach
    public void setup() {
        // Clear MongoDB collections to ensure test isolation
        if (mongoTemplate != null) {
            mongoTemplate.getCollectionNames().forEach(collectionName -> {
                if (!collectionName.startsWith("system.")) {
                    mongoTemplate.getCollection(collectionName).drop();
                }
            });
        }
    }
    
    /**
     * Cleanup logic after each test.
     */
    @AfterEach
    public void cleanup() {
        // Any additional cleanup code
    }
} 