# Testing Documentation

## Overview

This document outlines the testing strategy and approach for the UserService1B application, covering unit tests, integration tests, and API testing.

## Testing Strategy

### Test Pyramid

The testing approach follows the test pyramid principle:

1. **Unit Tests**: Test individual components in isolation
2. **Integration Tests**: Test component interactions
3. **API Tests**: Test the application through its REST API

### Test Environment

- **Test Database**: MongoDB test instance
- **Test Profile**: Uses `application-test.properties` configuration
- **Test Data**: Generated during test execution

## Unit Tests

### Overview

Unit tests focus on testing individual components in isolation, using mocks for dependencies.

### Service Layer Tests

#### UserServiceTest

Tests for the `UserService` implementation:

```java
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Test
    void testSaveUser_Success() {
        // Test implementation
    }
    
    @Test
    void testGetUserByEmail_UserExists() {
        // Test implementation
    }
    
    @Test
    void testGetUserById_UserNotFound() {
        // Test implementation
    }
}
```

#### AuthServiceTest

Tests for the `AuthenticationService` implementation:

```java
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JWTUtil jwtUtil;
    
    @InjectMocks
    private AuthenticationServiceImpl authService;
    
    @Test
    void testAuthenticate_Success() {
        // Test implementation
    }
    
    @Test
    void testAuthenticate_InvalidCredentials() {
        // Test implementation
    }
}
```

#### JWTUtilTest

Tests for the JWT utility class:

```java
public class JWTUtilTest {
    private JWTUtil jwtUtil;
    
    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil("test-secret-key", 3600000, 604800000);
    }
    
    @Test
    void testGenerateToken_ValidUser() {
        // Test implementation
    }
    
    @Test
    void testValidateToken_ValidToken() {
        // Test implementation
    }
}
```

## Integration Tests

### Repository Tests

Tests for repository layer with MongoDB:

```java
@DataMongoTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testFindByEmail_UserExists() {
        // Test implementation
    }
}
```

### Service Integration Tests

Tests service layer with actual repositories:

```java
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testSaveAndRetrieveUser() {
        // Test implementation
    }
}
```

### Controller Integration Tests

Tests controllers with MockMvc:

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testGetUserProfile_Authenticated() {
        // Test implementation
    }
}
```

## API Tests

### API Integration Tests

Tests the complete API flow:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApiIntegrationTests {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testUserRegistrationAndLogin() {
        // Test implementation
    }
}
```

### Manual API Testing

For manual API testing, use the provided Postman collection in the [postman directory](../postman/).

## Test Coverage

### Coverage Targets

- **Service Layer**: 90%+ coverage
- **Repository Layer**: 80%+ coverage
- **Controller Layer**: 80%+ coverage
- **Utility Classes**: 90%+ coverage

### Generating Coverage Reports

```bash
./mvnw clean test jacoco:report
```

Coverage reports are generated in `target/site/jacoco/index.html`.

## Test Execution

### Running All Tests

```bash
./mvnw clean test
```

### Running Specific Test Categories

```bash
# Run only unit tests
./mvnw clean test -Dtest=*Test

# Run only integration tests
./mvnw clean test -Dtest=*IntegrationTest
```

## Related Documents

- [Refresh Token Testing](../refresh_token.md#testing-guide) - Testing guide for refresh token functionality 