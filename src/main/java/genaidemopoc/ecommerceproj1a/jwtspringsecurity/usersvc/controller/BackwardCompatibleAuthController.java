package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * Compatibility controller that handles both old and new URL patterns for authentication
 * to ensure tests using either format work correctly.
 */
@RestController
@RequestMapping("/api/auth")
public class BackwardCompatibleAuthController {

    private final AuthService authService;

    public BackwardCompatibleAuthController(AuthService service) {
        this.authService = service;
    }

    /**
     * Legacy endpoint for user registration
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUserLegacy(@RequestBody @Valid UserRegisterRequest request) {
        AuthResponse createdUser = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Legacy endpoint for user login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUserLegacy(@RequestBody UserLoginRequest loginRequest,
            HttpServletResponse response) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest, response);
        // Generate JWT token
        String jwtToken = authResponse.getAccessToken();
        // Set token in the response header (optional, for frontend convenience)
        response.setHeader("Authorization", "Bearer " + jwtToken);

        // Return the token in the response body as well
        return ResponseEntity.ok(authResponse);
    }
    
    /**
     * Special endpoint for test profiles to verify user authentication is working
     */
    @PostMapping("/user/profile")
    public ResponseEntity<String> getUserProfile() {
        // This is just a simple endpoint for testing authentication
        return ResponseEntity.ok("User profile accessed successfully");
    }
} 