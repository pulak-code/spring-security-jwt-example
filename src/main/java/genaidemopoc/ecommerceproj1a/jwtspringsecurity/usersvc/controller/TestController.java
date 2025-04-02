package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.AuthResponse;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test controller for validating endpoints and authentication flow
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final AuthService authService;
    
    public TestController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * Basic status endpoint
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        log.debug("Test status endpoint accessed");
        return ResponseEntity.ok("Test controller is working");
    }
    
    /**
     * Test registration endpoint
     */
    @PostMapping(value = "/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        log.debug("Test register endpoint accessed with email: {}", request.getEmail());
        try {
            AuthResponse createdUser = authService.registerUser(request);
            log.debug("Registration successful for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            log.error("Error in test register: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Test login endpoint
     */
    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody UserLoginRequest loginRequest,
            HttpServletResponse response) {
        log.debug("Test login endpoint accessed with email: {}", loginRequest.getEmail());
        try {
            AuthResponse authResponse = authService.authenticateUser(loginRequest, response);
            
            // Generate JWT token
            String jwtToken = authResponse.getAccessToken();
            // Set token in the response header (optional, for frontend convenience)
            response.setHeader("Authorization", "Bearer " + jwtToken);

            log.debug("Login successful for email: {}", loginRequest.getEmail());
            // Return the token in the response body as well
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            log.error("Error in test login: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Test user profile endpoint
     */
    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        log.debug("Test user profile endpoint accessed");
        return ResponseEntity.ok("User profile accessed successfully");
    }
} 