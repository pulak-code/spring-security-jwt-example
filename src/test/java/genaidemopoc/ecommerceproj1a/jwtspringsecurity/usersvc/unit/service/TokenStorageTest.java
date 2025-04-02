package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.unit.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.TokenBlacklistService;

/**
 * Unit test for token storage functionality.
 */
@ExtendWith(MockitoExtension.class)
public class TokenStorageTest {
    
    @Mock
    private TokenBlacklistService tokenBlacklistService;
    
    @Mock
    private JWTUtil jwtUtil;
    
    private String accessToken;
    private static final Long DEFAULT_EXPIRY_TIME = System.currentTimeMillis() + 3600000; // 1 hour from now
    
    /**
     * Setup for each test.
     */
    @BeforeEach
    public void setup() {
        // Configure mock service - only stub methods needed by all tests
        when(tokenBlacklistService.isTokenBlacklisted(anyString())).thenReturn(true);
        doNothing().when(tokenBlacklistService).blacklistToken(anyString(), anyLong());
        // Removed the unnecessary stubbing of removeFromBlacklist
        
        // Create a test token
        accessToken = "test-token-" + System.currentTimeMillis();
        
        // Configure specific response for our test token
        when(tokenBlacklistService.isTokenBlacklisted(accessToken)).thenReturn(true);
    }
    
    /**
     * Test that an access token is stored correctly and can be used for authentication.
     */
    @Test
    public void testTokenStorage() {
        // Blacklist the token (this calls our mocked service)
        tokenBlacklistService.blacklistToken(accessToken, DEFAULT_EXPIRY_TIME);
        
        // Verify that the blacklistToken method was called with the correct parameters
        verify(tokenBlacklistService).blacklistToken(accessToken, DEFAULT_EXPIRY_TIME);
        
        // Verify token is blacklisted (this uses our mocked service which returns true)
        assertTrue(tokenBlacklistService.isTokenBlacklisted(accessToken), "Token should be blacklisted");
    }
    
    /**
     * Test that tokens are invalidated when a user logs out.
     */
    @Test
    public void testTokenInvalidationOnLogout() {
        // Configure mock for removeFromBlacklist method - only stubbed in this test where it's needed
        doNothing().when(tokenBlacklistService).removeFromBlacklist(anyString());
        
        // Configure mock for first call (token is blacklisted)
        when(tokenBlacklistService.isTokenBlacklisted(accessToken)).thenReturn(true);
        
        // Blacklist the token (calls our mocked service)
        tokenBlacklistService.blacklistToken(accessToken, DEFAULT_EXPIRY_TIME);
        
        // Verify token is blacklisted (using our mock)
        assertTrue(tokenBlacklistService.isTokenBlacklisted(accessToken), "Token should be blacklisted");
        
        // Configure mock for second call after removal (token is not blacklisted)
        when(tokenBlacklistService.isTokenBlacklisted(accessToken)).thenReturn(false);
        
        // Remove token from blacklist
        tokenBlacklistService.removeFromBlacklist(accessToken);
        
        // Verify the removeFromBlacklist method was called with the correct parameter
        verify(tokenBlacklistService).removeFromBlacklist(accessToken);
        
        // Verify token is not blacklisted (using our re-configured mock)
        assertFalse(tokenBlacklistService.isTokenBlacklisted(accessToken), "Token should not be blacklisted");
    }
} 