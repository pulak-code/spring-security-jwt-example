# MongoDB Refresh Token Testing Guide

## Overview
This document provides a comprehensive guide for testing the MongoDB-based refresh token implementation, including curl commands, Postman collection examples, and descriptions of relevant integration and unit tests.

## Prerequisites
- MongoDB running locally on port 27017 (or configured in application properties)
- UserService1B application running on `http://localhost:8081`
- A tool like curl, Postman, or Insomnia for API testing

## Testing with cURL Commands

### 1. Register a User
```bash
curl -X POST "http://localhost:8081/api/auth/user/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test@123","name":"Test User","roles":["ROLE_USER"]}'
```

### 2. Log In to Get Refresh Token
```bash
curl -X POST "http://localhost:8081/api/auth/user/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"Test@123"}' \
  -v
```
Note the `-v` flag to view the headers, where you'll find the refresh token in the response and cookie.

### 3. Verify Token Stored in MongoDB
```bash
# Using mongo shell
mongo
use UserDb
db.refresh_tokens.find({userEmail: "test@example.com"})

# Or using mongoexport
mongoexport --db UserDb --collection refresh_tokens --query '{"userEmail":"test@example.com"}' --out tokens.json
```

### 4. Use Refresh Token to Get New Access Token
```bash
curl -X POST "http://localhost:8081/api/auth/refresh-token" \
  -H "Content-Type: application/json" \
  -b "refreshToken=YOUR_REFRESH_TOKEN_VALUE" \
  -v
```
Replace `YOUR_REFRESH_TOKEN_VALUE` with the actual token value from the login response.

### 5. Logout (Delete Single Token)
```bash
curl -X POST "http://localhost:8081/api/auth/logout" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -b "refreshToken=YOUR_REFRESH_TOKEN_VALUE"
```

### 6. Verify Token Deleted from MongoDB
```bash
mongo
use UserDb
db.refresh_tokens.find({userEmail: "test@example.com"})
```
The token should no longer exist in the database.

### 7. Logout from All Devices (Delete All User Tokens)
```bash
curl -X POST "http://localhost:8081/api/auth/logout-all" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Postman Collection

### Setup

1. Create a new collection called "MongoDB Refresh Token Tests"
2. Create environment variables:
   - `base_url`: http://localhost:8081
   - `access_token`: (Empty, will be filled during testing)
   - `refresh_token`: (Empty, will be filled during testing)
   - `user_email`: test@example.com
   - `user_password`: Test@123

### Requests to Include

#### 1. Register User
- Method: POST
- URL: {{base_url}}/api/auth/user/register
- Body:
  ```json
  {
    "email": "{{user_email}}",
    "password": "{{user_password}}",
    "name": "Test User",
    "roles": ["ROLE_USER"]
  }
  ```

#### 2. Login
- Method: POST
- URL: {{base_url}}/api/auth/user/login
- Body:
  ```json
  {
    "email": "{{user_email}}",
    "password": "{{user_password}}"
  }
  ```
- Tests:
  ```javascript
  var jsonData = pm.response.json();
  if (jsonData.accessToken) {
      pm.environment.set("access_token", jsonData.accessToken);
  }
  
  if (jsonData.refreshToken) {
      pm.environment.set("refresh_token", jsonData.refreshToken);
  }
  
  // Also get cookie if refresh token is in cookie
  var cookieJar = pm.cookies.jar();
  cookieJar.get("localhost", "refreshToken", function(error, cookie) {
      if (!error && cookie) {
          pm.environment.set("refresh_token_cookie", cookie);
      }
  });
  ```

#### 3. Verify Refresh Token in DB
- This is a manual step using MongoDB client

#### 4. Refresh Access Token
- Method: POST
- URL: {{base_url}}/api/auth/refresh-token
- Headers:
  - Refresh-Token: {{refresh_token}}
- Tests:
  ```javascript
  var jsonData = pm.response.json();
  if (jsonData.accessToken) {
      pm.environment.set("access_token", jsonData.accessToken);
  }
  if (jsonData.refreshToken) {
      pm.environment.set("refresh_token", jsonData.refreshToken);
  }
  ```

#### 5. Access Protected Resource
- Method: GET
- URL: {{base_url}}/api/users/profile
- Headers:
  - Authorization: Bearer {{access_token}}

#### 6. Logout
- Method: POST
- URL: {{base_url}}/api/auth/logout
- Headers:
  - Authorization: Bearer {{access_token}}
  - Refresh-Token: {{refresh_token}}

#### 7. Verify Token Removed
- Manual step using MongoDB client

#### 8. Login Again for Multiple Device Test
- Same as Login request above

#### 9. Logout All Devices
- Method: POST
- URL: {{base_url}}/api/auth/logout-all
- Headers:
  - Authorization: Bearer {{access_token}}

## Integration Tests

Key integration tests for the refresh token functionality:

### RefreshTokenRepositoryIntegrationTest

```java
@SpringBootTest
@ActiveProfiles("test")
public class RefreshTokenRepositoryIntegrationTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Test
    public void testCreateAndFindToken() {
        // Create a token
        RefreshToken token = new RefreshToken();
        token.setToken("test-token");
        token.setUserId("user-123");
        token.setUserEmail("test@example.com");
        token.setCreatedAt(Instant.now());
        token.setExpiryDate(Instant.now().plusSeconds(3600));
        
        // Save to DB
        refreshTokenRepository.save(token);
        
        // Find by token
        Optional<RefreshToken> found = refreshTokenRepository.findByToken("test-token");
        assertTrue(found.isPresent());
        assertEquals("user-123", found.get().getUserId());
    }
    
    @Test
    public void testDeleteByToken() {
        // Create and save a token
        RefreshToken token = new RefreshToken();
        token.setToken("to-delete");
        token.setUserId("user-123");
        token.setUserEmail("test@example.com");
        token.setCreatedAt(Instant.now());
        token.setExpiryDate(Instant.now().plusSeconds(3600));
        refreshTokenRepository.save(token);
        
        // Delete it
        long deleted = refreshTokenRepository.deleteByToken("to-delete");
        assertEquals(1, deleted);
        
        // Verify it's gone
        Optional<RefreshToken> found = refreshTokenRepository.findByToken("to-delete");
        assertFalse(found.isPresent());
    }
    
    @Test
    public void testFindByUserId() {
        // Create multiple tokens for the same user
        for (int i = 0; i < 3; i++) {
            RefreshToken token = new RefreshToken();
            token.setToken("multi-" + i);
            token.setUserId("multi-user");
            token.setUserEmail("multi@example.com");
            token.setCreatedAt(Instant.now());
            token.setExpiryDate(Instant.now().plusSeconds(3600));
            refreshTokenRepository.save(token);
        }
        
        // Find all tokens for the user
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId("multi-user");
        assertEquals(3, tokens.size());
    }
    
    @Test
    public void testDeleteByUserId() {
        // Create tokens for a specific user
        for (int i = 0; i < 3; i++) {
            RefreshToken token = new RefreshToken();
            token.setToken("delete-multi-" + i);
            token.setUserId("to-delete-user");
            token.setUserEmail("todelete@example.com");
            token.setCreatedAt(Instant.now());
            token.setExpiryDate(Instant.now().plusSeconds(3600));
            refreshTokenRepository.save(token);
        }
        
        // Delete all for user
        long deleted = refreshTokenRepository.deleteByUserId("to-delete-user");
        assertEquals(3, deleted);
        
        // Verify all gone
        List<RefreshToken> remaining = refreshTokenRepository.findByUserId("to-delete-user");
        assertEquals(0, remaining.size());
    }
}
```

### RefreshTokenServiceIntegrationTest

```java
@SpringBootTest
@ActiveProfiles("test")
public class RefreshTokenServiceIntegrationTest {

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Test
    public void testCreateRefreshToken() {
        // Create a token
        RefreshToken token = refreshTokenService.createRefreshToken(
            "user-service-test", 
            "service@example.com", 
            "test-service-token", 
            Instant.now().plusSeconds(3600)
        );
        
        assertNotNull(token);
        assertNotNull(token.getId());
        assertEquals("user-service-test", token.getUserId());
    }
    
    @Test
    public void testFindAndVerifyToken() {
        // Create a token
        String tokenValue = "verify-test-token";
        refreshTokenService.createRefreshToken(
            "verify-user", 
            "verify@example.com", 
            tokenValue, 
            Instant.now().plusSeconds(3600)
        );
        
        // Verify it
        boolean valid = refreshTokenService.verifyExpiration(tokenValue);
        assertTrue(valid);
        
        // Find it
        Optional<RefreshToken> found = refreshTokenService.findByToken(tokenValue);
        assertTrue(found.isPresent());
    }
    
    @Test
    public void testExpiredToken() {
        // Create an expired token
        String tokenValue = "expired-token";
        refreshTokenService.createRefreshToken(
            "expired-user", 
            "expired@example.com", 
            tokenValue, 
            Instant.now().minusSeconds(3600) // Already expired
        );
        
        // Verify it's invalid
        boolean valid = refreshTokenService.verifyExpiration(tokenValue);
        assertFalse(valid);
    }
}
```

### AuthControllerIntegrationTest

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @LocalServerPort
    private int port;
    
    private String getBaseUrl() {
        return "http://localhost:" + port;
    }
    
    @Test
    public void testLoginCreatesRefreshToken() {
        // Register a user
        UserRegisterRequest register = new UserRegisterRequest();
        register.setEmail("auth-test@example.com");
        register.setPassword("Auth@123");
        register.setName("Auth Test");
        register.setRoles(List.of("ROLE_USER"));
        
        restTemplate.postForEntity(
            getBaseUrl() + "/api/auth/user/register",
            register,
            Object.class
        );
        
        // Login the user
        LoginRequest login = new LoginRequest();
        login.setEmail("auth-test@example.com");
        login.setPassword("Auth@123");
        
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            getBaseUrl() + "/api/auth/user/login",
            login,
            LoginResponse.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getRefreshToken());
        
        // Check if token is in the database
        List<RefreshToken> tokens = refreshTokenRepository.findByUserEmail("auth-test@example.com");
        assertFalse(tokens.isEmpty());
    }
    
    @Test
    public void testLogoutDeletesRefreshToken() {
        // Create a user and get tokens
        // ... (similar to testLoginCreatesRefreshToken)
        
        // Logout
        // ... (call logout endpoint)
        
        // Verify token is deleted from DB
        // ... (check DB using repository)
    }
}
```

## Unit Tests

Key unit tests for the refresh token functionality:

### RefreshTokenServiceTest

```java
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;
    
    @Test
    public void testCreateRefreshToken() {
        // Setup
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> {
            RefreshToken token = i.getArgument(0);
            token.setId("generated-id");
            return token;
        });
        
        // Execute
        RefreshToken result = refreshTokenService.createRefreshToken(
            "test-user", 
            "test@example.com", 
            "test-token", 
            Instant.now().plusSeconds(3600)
        );
        
        // Verify
        assertNotNull(result);
        assertEquals("generated-id", result.getId());
        assertEquals("test-user", result.getUserId());
        assertEquals("test@example.com", result.getUserEmail());
        assertEquals("test-token", result.getToken());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }
    
    @Test
    public void testVerifyExpiration_ValidToken() {
        // Setup
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(3600));
        when(refreshTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
        
        // Execute
        boolean result = refreshTokenService.verifyExpiration("valid-token");
        
        // Verify
        assertTrue(result);
    }
    
    @Test
    public void testVerifyExpiration_ExpiredToken() {
        // Setup
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minusSeconds(3600));
        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));
        
        // Execute
        boolean result = refreshTokenService.verifyExpiration("expired-token");
        
        // Verify
        assertFalse(result);
        verify(refreshTokenRepository, times(1)).delete(any(RefreshToken.class));
    }
    
    @Test
    public void testVerifyExpiration_NonexistentToken() {
        // Setup
        when(refreshTokenRepository.findByToken("nonexistent-token")).thenReturn(Optional.empty());
        
        // Execute
        boolean result = refreshTokenService.verifyExpiration("nonexistent-token");
        
        // Verify
        assertFalse(result);
    }
}
```

## MongoDB TTL Index Testing

To verify that the MongoDB TTL index is working correctly:

1. Create a token with a short expiration time (e.g., 5 seconds):

```java
RefreshToken token = new RefreshToken();
token.setToken("ttl-test-token");
token.setUserId("ttl-user");
token.setUserEmail("ttl@example.com");
token.setCreatedAt(Instant.now());
// Set expiry date to 5 seconds from now
token.setExpiryDate(Instant.now().plusSeconds(5));
refreshTokenRepository.save(token);
```

2. Verify token exists in the database:

```bash
mongo
use UserDb
db.refresh_tokens.find({token: "ttl-test-token"})
```

3. Wait for at least 60 seconds (MongoDB's TTL monitor runs every 60 seconds):

```bash
sleep 60
```

4. Check if the token has been removed:

```bash
mongo
use UserDb
db.refresh_tokens.find({token: "ttl-test-token"})
```

The result should be an empty set as the document should have been automatically removed. 