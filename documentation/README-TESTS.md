# Integration Tests for User Service

This document describes the integration tests for the User Service application, which handles user authentication, JWT token management, and user administration.

## Test Structure

The integration tests are organized into several test classes that cover different aspects of the application:

1. **ApiIntegrationTests** - Tests the basic API functionality, including user registration, login, search, and logout.
2. **TokenRefreshIntegrationTest** - Tests the JWT token refresh functionality.
3. **TokenPerformanceTest** - Tests the performance of token generation and validation.
4. **TokenStorageTest** - Tests the token storage and invalidation functionality.

## Running Tests

### Prerequisites

- Java 17 or higher
- Maven
- MongoDB running on localhost:27017

### Using the Shell Script

The easiest way to run all integration tests is to use the provided shell script:

```bash
# Make the script executable if not already
chmod +x run_tests.sh

# Run the tests
./run_tests.sh
```

### Running Individual Tests

To run a specific test class:

```bash
mvn test -Dtest=ApiIntegrationTests
mvn test -Dtest=TokenRefreshIntegrationTest
mvn test -Dtest=TokenPerformanceTest
mvn test -Dtest=TokenStorageTest
```

### Test Profile

The tests use a dedicated `test` profile defined in `application-test.properties`, which configures:

- A different server port (8082) to avoid conflicts with a running application
- Test-specific JWT settings
- A separate MongoDB database for testing

## Test Helper Classes

### TestHelper

The `TestHelper` class provides utility methods for:
- Creating user registration DTOs
- Creating authentication requests
- Setting up HTTP headers
- Making authenticated HTTP requests

### TestConfig

The `TestConfig` class provides test-specific configuration beans:
- A `RestTemplate` for making HTTP requests
- A `MockRestServiceServer` for mocking external API calls

## Test Coverage

The integration tests cover the following functionality:

### Authentication
- User registration
- User login and token generation
- Token refresh
- Logout (single device)
- Logout from all devices

### Authorization
- Access control for admin-only endpoints
- Role-based permissions

### Token Management
- Token storage
- Token validation
- Token revocation
- Multiple active tokens per user

### Performance
- Token generation performance
- Token validation performance
- Concurrent user handling

## Troubleshooting

If tests fail, check:

1. MongoDB is running and accessible
2. No other application is using the test port (8082)
3. The test profile is correctly loaded

## Extending the Tests

To add new tests:

1. Create a new test class in the `genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.integration` package
2. Use the `@SpringBootTest` and `@ActiveProfiles("test")` annotations
3. Utilize the `TestHelper` class for common operations
4. Add the new test to the `run_tests.sh` script 