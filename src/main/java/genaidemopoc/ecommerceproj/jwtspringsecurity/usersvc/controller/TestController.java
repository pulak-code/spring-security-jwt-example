package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;

@RestController
@RequestMapping(AppConstants.AUTH_BASE_PATH + "/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
    	logger.info("GET /hello endpoint called");
        return ResponseEntity.ok("Hello from test controller");
    }
} 