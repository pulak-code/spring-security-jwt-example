# User Service API Documentation

## Available Documentation Files

1. **[Endpoint Testing Guide](endpoint_testing_guide.md)**
   - Comprehensive guide for testing all API endpoints
   - Includes authentication flow, user management, and administrative operations
   - Provides curl commands for each endpoint

2. **[Security Methods Flow](security_methods_flow.md)**
   - Detailed explanation of the security implementation
   - Describes JWT authentication process and token management
   - Documents all security-related classes and methods
   - Includes authentication flow diagram

3. **[API Documentation](api-docs.json)**
   - OpenAPI specification in JSON format
   - Can be imported into Swagger UI or Postman

4. **[JWT Configuration Guide](jwt_configuration.md)**
   - Details about JWT token implementation
   - Configuration properties and their usage
   - Best practices for JWT secret management

## Additional Resources

- The main README file in the project root contains an overview of the project, recent fixes, and setup instructions.
- The application's Swagger UI is available at `http://localhost:8081/swagger-ui/index.html` when the application is running.

## Testing Scripts

The repository includes test scripts to help verify the API functionality:

- **[test_endpoints.sh](../test_endpoints.sh)** - Tests the search by email and logout-all endpoints
- **[test_api.sh](../test_api.sh)** - A more comprehensive script for testing all API endpoints

To run the test scripts:

```bash
chmod +x test_endpoints.sh
./test_endpoints.sh
``` 