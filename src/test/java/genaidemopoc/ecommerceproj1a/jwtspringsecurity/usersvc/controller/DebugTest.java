package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class DebugTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void debugAuthController() throws Exception {
        try {
            // Create a register request
            UserRegisterRequest registerRequest = UserRegisterRequest.builder()
                    .email("debug_test@example.com")
                    .password("password123")
                    .name("Debug User")
                    .build();
            
            // First, try with the first format of the endpoint
            System.out.println("TESTING ENDPOINT: /api/auth/user/register");
            try {
                MvcResult result = mockMvc.perform(post("/api/auth/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andReturn();
                
                System.out.println("STATUS: " + result.getResponse().getStatus());
                System.out.println("RESPONSE: " + result.getResponse().getContentAsString());
                if (result.getResolvedException() != null) {
                    System.out.println("EXCEPTION: " + result.getResolvedException().getMessage());
                    result.getResolvedException().printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("ERROR TESTING FIRST ENDPOINT: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Then try the second format
            System.out.println("\nTESTING ENDPOINT: /api/auth/register");
            try {
                MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andReturn();
                
                System.out.println("STATUS: " + result.getResponse().getStatus());
                System.out.println("RESPONSE: " + result.getResponse().getContentAsString());
                if (result.getResolvedException() != null) {
                    System.out.println("EXCEPTION: " + result.getResolvedException().getMessage());
                    result.getResolvedException().printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("ERROR TESTING SECOND ENDPOINT: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Now test simplified auth controller for comparison
            System.out.println("\nTESTING ENDPOINT: /api/simple-auth/register");
            try {
                MvcResult result = mockMvc.perform(post("/api/simple-auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andReturn();
                
                System.out.println("STATUS: " + result.getResponse().getStatus());
                System.out.println("RESPONSE: " + result.getResponse().getContentAsString());
                if (result.getResolvedException() != null) {
                    System.out.println("EXCEPTION: " + result.getResolvedException().getMessage());
                    result.getResolvedException().printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("ERROR TESTING SIMPLIFIED ENDPOINT: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("GENERAL ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 