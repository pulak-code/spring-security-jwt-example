package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Simple test to verify that our REST endpoints are properly configured
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class VerifyEndpointsConfigured {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void verifyStatusEndpoint() throws Exception {
        // Use the standard Actuator health endpoint
        MvcResult result = mockMvc.perform(get("/actuator/health")) 
                .andExpect(status().isOk())
                .andReturn();
        
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        // Optionally check the response body for status UP
        assertThat(result.getResponse().getContentAsString()).contains("\"status\":\"UP\"");
    }

    @Test
    public void verifyAuthEndpoints() throws Exception {
        // Verify login endpoint exists
        mockMvc.perform(get("/users/api/auth/user/login"))
                .andExpect(status().isMethodNotAllowed());

        // Verify register endpoint exists
        mockMvc.perform(get("/users/api/auth/user/register"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void verifyAdminEndpoints() throws Exception {
        // Verify admin endpoint exists
        mockMvc.perform(get("/api/admin/user/all"))
                .andExpect(status().isUnauthorized());
    }
} 