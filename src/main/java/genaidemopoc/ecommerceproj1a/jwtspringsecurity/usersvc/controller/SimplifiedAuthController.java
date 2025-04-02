package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserLoginRequest;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.dto.UserRegisterRequest;

@RestController
@RequestMapping("/api/simple-auth")
public class SimplifiedAuthController {

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok("Registered user: " + request.getEmail());
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok("Logged in user: " + request.getEmail());
    }
} 