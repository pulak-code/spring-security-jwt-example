package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

/**
 * Simplified REST controller for testing purposes
 */
@RestController
@RequestMapping(path = "/rest-test", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestTestController {
    
    private static final Logger log = LoggerFactory.getLogger(RestTestController.class);
    
    public RestTestController() {
        log.info("RestTestController initialized");
    }
    
    /**
     * Basic status endpoint
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        log.info(LoggingUserServiceConstants.LOG_REQUEST_START, "GET", "/rest-test/status");
        return ResponseEntity.ok("RestTestController is working");
    }
    
    /**
     * Test registration endpoint - simplified for testing
     */
    @PostMapping(path = "/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterRequest request) {
        log.info(LoggingUserServiceConstants.LOG_REQUEST_START, "POST", "/rest-test/auth/register");
        try {
            // Return a successful response without actually registering
            log.info(LoggingUserServiceConstants.LOG_USER_CREATED, request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"message\":\"" + MessageUserServiceConstants.USER_CREATED_SUCCESS + "\"}");
        } catch (Exception e) {
            log.error(LoggingUserServiceConstants.LOG_EXCEPTION, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Test login endpoint - simplified for testing
     */
    @PostMapping(path = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticateUser(@RequestBody UserLoginRequest loginRequest,
            HttpServletResponse response) {
        log.info(LoggingUserServiceConstants.LOG_REQUEST_START, "POST", "/rest-test/auth/login");
        try {
            // Simulate JWT token
            String jwtToken = "simulated-jwt-token-for-testing-" + System.currentTimeMillis();
            // Set token in the response header
            response.setHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + jwtToken);

            log.info(LoggingUserServiceConstants.LOG_AUTH_SUCCESS, loginRequest.getEmail());
            return ResponseEntity.ok("{\"message\":\"" + MessageUserServiceConstants.LOGIN_SUCCESS 
                + "\",\"token\":\"" + jwtToken + "\"}");
        } catch (Exception e) {
            log.error(LoggingUserServiceConstants.LOG_EXCEPTION, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Test user profile endpoint
     */
    @GetMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        log.info(LoggingUserServiceConstants.LOG_REQUEST_START, "GET", "/rest-test/user/profile");
        return ResponseEntity.ok("{\"message\":\"" + MessageUserServiceConstants.API_SUCCESS + "\"}");
    }
} 