# Test Documentation

This document outlines the testing strategy for the JWT Security User Service application, explaining the purpose and scope of each test class.

## Testing Strategy

Our testing approach follows a pyramid structure:
- **Unit Tests**: Fast tests that validate business logic in isolation
- **Integration Tests**: Validate component interactions with minimal mocking
- **End-to-End Tests**: Validate complete business scenarios

## Unit Tests

Located in `src/test/java/genaidemopoc/ecommerceproj1a/jwtspringsecurity/usersvc/unit/`

### Controller Tests

#### `AuthControllerTest.java` (Planned)
- Tests the authentication controller endpoints in isolation
- Mocks: AuthService
- Tests:
  - User registration endpoint
  - User login endpoint
  - Token refresh endpoint
  - Logout endpoint
  - Logout all devices endpoint
  - Error handling for invalid requests

#### `RestTestControllerTest.java` (Planned)
- Tests the test controller endpoints in isolation
- Tests:
  - Status endpoint
  - Test registration endpoint
  - Test login endpoint
  - Test user profile endpoint

### Service Tests

#### `AuthServiceTest.java` (Planned)
- Tests authentication service logic in isolation
- Mocks: AuthenticationManager, JWTUtil, TokenBlacklistService, UserService, TokenStorageService
- Tests:
  - User authentication
  - Token refresh
  - User logout
  - Logout from all devices
  - User registration
  - Error conditions (invalid credentials, expired tokens)

#### `TokenStorageTest.java` (Implemented)
- Tests token storage functionality
- Mocks: TokenBlacklistService
- Tests:
  - Token storage
  - Token invalidation on logout

#### `TokenBlacklistServiceTest.java` (Planned)
- Tests token blacklisting functionality
- Mocks: JWTUtil
- Tests:
  - Adding tokens to blacklist
  - Checking if tokens are blacklisted
  - Removing tokens from blacklist
  - Cleanup of expired tokens

#### `UserServiceTest.java` (Planned)
- Tests user management functionality
- Mocks: UserRepository, PasswordEncoder
- Tests:
  - User creation
  - User retrieval
  - User existence checks
  - User role management

### Security Tests

#### `SecurityConfigTest.java` (Implemented - Structure Only)
- Tests security configuration settings
- Tests:
  - Security filter chain configuration
  - CORS configuration
  - Authentication providers configuration

#### `JWTUtilTest.java` (Planned)
- Tests JWT token utility functions
- Tests:
  - Token generation
  - Token validation
  - Token parsing
  - Claims extraction
  - Refresh token generation
  - Token expiration handling

#### `CustomUserDetailsServiceTest.java` (Planned)
- Tests custom user details service implementation
- Mocks: UserRepository
- Tests:
  - Loading user by username
  - Handling non-existent users

### Filter Tests

#### `JWTAuthenticationFilterTest.java` (Planned)
- Tests JWT authentication filter
- Mocks: FilterChain, HttpServletRequest, HttpServletResponse, JWTUtil
- Tests:
  - Token extraction
  - Authentication process
  - Error handling

#### `CorsFilterTest.java` (Planned)
- Tests CORS filter functionality
- Mocks: FilterChain, HttpServletRequest, HttpServletResponse
- Tests:
  - CORS headers
  - Preflight requests

### Utility Tests

#### `ValidationUtilTest.java` (Planned)
- Tests input validation utilities
- Tests:
  - Email validation
  - Password validation
  - Name validation
  - Role validation

## Integration Tests

Located in `src/test/java/genaidemopoc/ecommerceproj1a/jwtspringsecurity/usersvc/integration/`

#### `ApiIntegrationTests.java` (Implemented)
- Tests complete API flows with real HTTP requests
- Tests:
  - User registration flow
  - User login flow
  - Protected resource access with token
  - Invalid token handling
  - Token expiration handling
  - Token refresh flow

#### `TokenRefreshIntegrationTest.java` (Implemented)
- Tests token refresh flows end-to-end
- Tests:
  - Refresh token usage
  - Multiple refresh token uses
  - Refresh token rotation
  - Refresh token expiration
  - Refresh after logout failure

#### `VerifyEndpointsConfigured.java` (Implemented)
- Tests that REST endpoints are properly configured
- Tests:
  - Endpoint accessibility
  - Expected HTTP status codes
  - Response content verification

## Configuration and Setup

### Test Profiles

- **test**: Basic test configuration with in-memory databases
- **integration**: Configuration for integration tests

### Test Constants

Located in `src/test/java/genaidemopoc/ecommerceproj1a/jwtspringsecurity/usersvc/integration/constants/`

- **UserServiceTestConstants.java**: Test constants for users
- **LogMessageConstants.java**: Constants for log message assertions

## Running Tests

### Unit Tests
```
mvn test -Dtest=*Test
```

### Integration Tests
```
mvn test -Dtest=*IntegrationTest
bash run_tests.sh
```

### All Tests
```
mvn test
```

## Test Coverage

Current test coverage goals:
- Controllers: 90%
- Services: 95%
- Security: 95%
- Models: 80%
- Utilities: 90%

## Test Data

- Test users are created with random emails to avoid collisions
- Test passwords follow security requirements
- Default test user roles are USER or ADMIN

## Exception Testing

All tests include validation of:
- Happy path scenarios
- Error conditions
- Edge cases
- Security constraints

## Performance Testing

TokenPerformanceTest includes:
- Single operation latency tests
- Throughput tests
- Concurrent operation tests 