package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
} 