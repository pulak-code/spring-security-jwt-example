package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetStatus() throws Exception {
        MvcResult result = mockMvc.perform(get("/test/status")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Test controller is working"))
                .andReturn();
        
        System.out.println("=== TEST STATUS RESPONSE ===");
        System.out.println("Response: " + result.getResponse().getContentAsString());
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("==========================");
    }
    
    @Test
    public void debugAuthRoutes() throws Exception {
        // Just try to hit the auth endpoints and see what happens
        // We expect this test to fail, we're just debugging the routing
        MvcResult result = mockMvc.perform(post("/api/auth/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andReturn();
        
        System.out.println("=== DEBUG AUTH ROUTE TEST - REGISTER ===");
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Response: " + result.getResponse().getContentAsString());
        System.out.println("Handler: " + (result.getHandler() != null ? result.getHandler().toString() : "null"));
        System.out.println("Exception: " + (result.getResolvedException() != null ? result.getResolvedException().toString() : "null"));
        System.out.println("Exception message: " + (result.getResolvedException() != null ? result.getResolvedException().getMessage() : "null"));
        System.out.println("Request info: " + result.getRequest().getMethod() + " " + result.getRequest().getRequestURI());
        System.out.println("============================");
        
        // Also try login
        result = mockMvc.perform(post("/api/auth/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andReturn();
        
        System.out.println("=== DEBUG AUTH ROUTE TEST - LOGIN ===");
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Response: " + result.getResponse().getContentAsString());
        System.out.println("Handler: " + (result.getHandler() != null ? result.getHandler().toString() : "null"));
        System.out.println("Exception: " + (result.getResolvedException() != null ? result.getResolvedException().toString() : "null"));
        System.out.println("Exception message: " + (result.getResolvedException() != null ? result.getResolvedException().getMessage() : "null"));
        System.out.println("Request info: " + result.getRequest().getMethod() + " " + result.getRequest().getRequestURI());
        System.out.println("============================");
    }
    
    @Test
    public void testSimplifiedAuthController() throws Exception {
        // Test the simplified controller
        MvcResult result = mockMvc.perform(post("/api/simple-auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test User\",\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andReturn();
        
        System.out.println("=== SIMPLIFIED AUTH TEST - REGISTER ===");
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Response: " + result.getResponse().getContentAsString());
        System.out.println("Handler: " + (result.getHandler() != null ? result.getHandler().toString() : "null"));
        System.out.println("Exception: " + (result.getResolvedException() != null ? result.getResolvedException().toString() : "null"));
        System.out.println("Request info: " + result.getRequest().getMethod() + " " + result.getRequest().getRequestURI());
        System.out.println("============================");
        
        // Test login endpoint
        result = mockMvc.perform(post("/api/simple-auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andReturn();
        
        System.out.println("=== SIMPLIFIED AUTH TEST - LOGIN ===");
        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Response: " + result.getResponse().getContentAsString());
        System.out.println("Handler: " + (result.getHandler() != null ? result.getHandler().toString() : "null"));
        System.out.println("Exception: " + (result.getResolvedException() != null ? result.getResolvedException().toString() : "null"));
        System.out.println("Request info: " + result.getRequest().getMethod() + " " + result.getRequest().getRequestURI());
        System.out.println("============================");
    }
} 