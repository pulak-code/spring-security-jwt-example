package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.AuthService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * Debug controller for identifying issues with authentication endpoints
 */
@RestController
@RequestMapping("/debug")
public class DebugController {
    
    private static final Logger log = LoggerFactory.getLogger(DebugController.class);
    private final AuthService authService;
    
    public DebugController(AuthService authService) {
        this.authService = authService;
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Debug controller is running");
        log.info("Debug status endpoint accessed");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> debugRegister(@RequestBody @Valid UserRegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("Debug register endpoint accessed with email: {}", request.getEmail());
            
            // Call the auth service to register
            var result = authService.registerUser(request);
            
            response.put("success", true);
            response.put("message", "Registration debug successful");
            response.put("email", request.getEmail());
            response.put("serviceResponse", result);
            log.info("Registration debug successful for email: {}", request.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in debug register: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getName());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> debugLogin(@RequestBody UserLoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("Debug login endpoint accessed with email: {}", request.getEmail());
            
            // Note: We're not actually calling the auth service here to avoid affecting tokens
            response.put("success", true);
            response.put("message", "Login debug successful");
            response.put("email", request.getEmail());
            log.info("Login debug successful for email: {}", request.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in debug login: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("errorType", e.getClass().getName());
            return ResponseEntity.status(500).body(response);
        }
    }
} 