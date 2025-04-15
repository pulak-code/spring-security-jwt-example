# UserService1B - User Management Microservice

## Overview
UserService1B is a Spring Boot microservice that provides user management functionality with JWT-based authentication and role-based access control. It's part of an e-commerce system and handles user registration, authentication, and authorization.

## Table of Contents
1. [Architecture](#architecture)
2. [Database Schema](#database-schema)
3. [Class Diagram](#class-diagram)
4. [Flow Diagrams](#flow-diagrams)
5. [API Documentation](#api-documentation)
6. [Testing](#testing)
7. [Deployment](#deployment)
8. [Security](#security)

## Architecture

### System Architecture
```mermaid
graph TD
    A[Client] --> B[API Gateway]
    B --> C[UserService1B]
    C --> D[MongoDB]
    C --> E[Redis Cache]
    C --> F[Logging Service]
```

### Technology Stack
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT Authentication
- MongoDB
- Maven
- JUnit 5
- Mockito
- Swagger/OpenAPI

## Database Schema

### MongoDB Collections

#### User Collection
```mermaid
erDiagram
    UserEntity {
        String id
        String email
        String password
        String name
        List<String> roles
        List<String> addresses
        List<String> orderIds
        Instant createdAt
        Instant updatedAt
    }
```

#### RefreshToken Collection
```mermaid
erDiagram
    RefreshToken {
        String id
        String userId
        String userEmail
        String token
        Instant createdAt
        Instant expiryDate
    }
```

## Class Diagram

```mermaid
classDiagram
    class AuthController {
        +registerUser()
        +authenticateUser()
        +refreshToken()
        +logout()
        +logoutAll()
    }
    
    class AuthService {
        <<interface>>
        +authenticateUser()
        +registerUser()
        +refreshAccessTokens()
        +logout()
        +logoutAll()
    }
    
    class AuthServiceImpl {
        +authenticateUser()
        +registerUser()
        +refreshAccessTokens()
        +logout()
        +logoutAll()
    }
    
    class JWTUtil {
        +generateAccessToken()
        +generateRefreshToken()
        +validateToken()
        +extractUsername()
    }
    
    class UserService {
        <<interface>>
        +getUserByEmail()
        +saveUser()
        +updateUser()
        +deleteUser()
    }
    
    class UserServiceImpl {
        +getUserByEmail()
        +saveUser()
        +updateUser()
        +deleteUser()
    }
    
    AuthController --> AuthService
    AuthService <|.. AuthServiceImpl
    AuthServiceImpl --> JWTUtil
    AuthServiceImpl --> UserService
    UserService <|.. UserServiceImpl
```

## Flow Diagrams

### Authentication Flow
```mermaid
sequenceDiagram
    participant Client
    participant AuthController
    participant AuthService
    participant JWTUtil
    participant UserService
    
    Client->>AuthController: POST /api/auth/user/login
    AuthController->>AuthService: authenticateUser()
    AuthService->>UserService: getUserByEmail()
    UserService-->>AuthService: UserEntity
    AuthService->>JWTUtil: generateAccessToken()
    AuthService->>JWTUtil: generateRefreshToken()
    AuthService-->>AuthController: AuthResponse
    AuthController-->>Client: 200 OK with tokens
```

### User Registration Flow
```mermaid
sequenceDiagram
    participant Client
    participant AuthController
    participant AuthService
    participant UserService
    participant JWTUtil
    
    Client->>AuthController: POST /api/auth/user/register
    AuthController->>AuthService: registerUser()
    AuthService->>UserService: userExists()
    UserService-->>AuthService: false
    AuthService->>UserService: saveUser()
    AuthService->>JWTUtil: generateAccessToken()
    AuthService->>JWTUtil: generateRefreshToken()
    AuthService-->>AuthController: AuthResponse
    AuthController-->>Client: 201 Created
```

## API Documentation

### Base URL
```
http://localhost:8080
```

**Note:** Most endpoints require Bearer Token authentication using a JWT access token obtained via login. Endpoints marked (Admin) require a token from a user with the `ROLE_ADMIN`.

### Authentication Endpoints (`/api/auth/user`)

#### Register User
Registers a new user. Default role is `USER` if not specified.
```http
POST /api/auth/user/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "Password@1234",
    "name": "John Doe",
    "roles": ["USER"] // Optional, defaults to ["USER"]
}
```

#### Login
Authenticates a user and returns access and refresh tokens.
```http
POST /api/auth/user/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "Password@1234"
}
```

#### Refresh Token
Refreshes the access token using a valid refresh token (sent via secure, HttpOnly cookie).
```http
POST /api/auth/user/refresh
```
*(Note: Refresh token is typically handled automatically via cookie)*

#### Logout
Logs out the current user session (server-side doesn't invalidate JWT, relies on client deleting token).
```http
POST /api/auth/user/logout
Authorization: Bearer <access_token>
```

#### Logout All Devices
Invalidates all refresh tokens for the user associated with the provided refresh token (sent via header or body).
```http
POST /api/auth/user/logout-all
Refresh-Token: <refresh_token>
```

### User Endpoints (`/api/user`)

#### Get User Profile
Gets the profile of the currently authenticated user.
```http
GET /api/user/profile
Authorization: Bearer <access_token>
```

#### Get User By ID (Admin)
Gets the profile of any user by their ID. Requires Admin role.
```http
GET /api/user/{id}
Authorization: Bearer <admin_access_token>
```

#### Edit User Profile
Updates the profile of the currently authenticated user.
```http
PUT /api/user/edit/{id}
Authorization: Bearer <access_token>
Content-Type: application/json

{
    "name": "Updated Name",
    "addresses": ["New Address"] // Example field
}
```
*(Note: The `{id}` must match the authenticated user's ID)*


### Admin Endpoints (`/api/admin`) (Admin Role Required)

#### Admin Dashboard
A simple endpoint to check admin access.
```http
GET /api/admin/dashboard
Authorization: Bearer <admin_access_token>
```

#### Create Admin User
Creates a new user with `ROLE_ADMIN`. Can specify email/password/name or defaults will be used.
```http
POST /api/admin/users
Authorization: Bearer <admin_access_token>
Content-Type: application/json

// Example: Create default admin@example.com
{}

// Example: Create specific admin
{
    "email": "newadmin@example.com",
    "password": "Password@1234",
    "name": "New Admin"
}
```

#### Reset Default Admin Passwords
Resets passwords for `admin@example.com` and `admin99@example.com` to `Password@1234` and ensures they have `ROLE_ADMIN`.
```http
POST /api/admin/reset-password
Authorization: Bearer <admin_access_token>
```

### Admin User Management Endpoints (Admin Role Required)
*(Note: These are managed by AdminController but follow the /api/user path convention)*

#### Get All Users
Retrieves a list of all users.
```http
GET /api/user
Authorization: Bearer <admin_access_token>
```
*(Alternative: `GET /api/user/all`)*

#### Update Any User
Allows an admin to update any user's details by their ID.
```http
PUT /api/user/update/{userid}
Authorization: Bearer <admin_access_token>
Content-Type: application/json

{
    "name": "Updated User Name by Admin",
    "roles": ["USER", "EDITOR"] // Example: Update roles
}
```

#### Search Users
Searches for users by email or username.
```http
GET /api/user/search?email=user@example.com
Authorization: Bearer <admin_access_token>

GET /api/user/search?username=JohnD
Authorization: Bearer <admin_access_token>
```

#### Delete All Users
Deletes all users except the currently logged-in admin. **Use with extreme caution!**
```http
DELETE /api/user/all
Authorization: Bearer <admin_access_token>
```


### Test Endpoint (`/api/auth/test`)

#### Hello
A simple test endpoint.
```http
GET /api/auth/test/hello
```

### cURL Commands (Examples)

#### Register User
```bash
curl -X POST http://localhost:8080/api/auth/user/register \\
  -H "Content-Type: application/json" \\
  -d '{
    "email": "testuser@example.com",
    "password": "Password@1234",
    "name": "Test User"
  }'
```

#### Login (Admin)
```bash
# Note: Use admin@example.com or admin99@example.com after reset
curl -X POST http://localhost:8080/api/auth/user/login \\
  -H "Content-Type: application/json" \\
  -d '{
    "email": "admin@example.com",
    "password": "Password@1234"
  }'
```
*(Save the returned `accessToken`)*

#### Get All Users (Admin)
```bash
# Replace <admin_access_token> with the token from admin login
curl -X GET http://localhost:8080/api/user \\
  -H "Authorization: Bearer <admin_access_token>"
```

## Testing

### Unit Tests
```bash
./mvnw test
```

### Integration Tests
```bash
./mvnw test -Dtest=*IntegrationTest
```

### Test Coverage
```bash
./mvnw test -Djacoco.skip=false
```

### Postman Collection
[Download Postman Collection](docs/postman/UserService1B.postman_collection.json)

## Testing Guide

### Complete API Flow Testing Steps

Follow these steps in order to test the complete user authentication and management flow:

1. **Register a New User**
   ```bash
   curl -X POST http://localhost:8080/api/auth/user/register \
     -H "Content-Type: application/json" \
     -d '{
       "email": "testuser@example.com",
       "password": "Test@123",
       "name": "Test User",
       "roles": ["USER"]
     }'
   ```
   - Expected Response: 201 Created
   - Save the access token and refresh token from the response

2. **Login with Registered User**
   ```bash
   curl -X POST http://localhost:8080/api/auth/user/login \
     -H "Content-Type: application/json" \
     -d '{
       "email": "testuser@example.com",
       "password": "Test@123"
     }'
   ```
   - Expected Response: 200 OK with new tokens
   - Save the new access token and refresh token

3. **Get User Profile**
   ```bash
   curl -X GET http://localhost:8080/api/users/profile \
     -H "Authorization: Bearer <access_token>"
   ```
   - Expected Response: 200 OK with user details
   - Verify the returned user information matches the registration data

4. **Update User Profile**
   ```bash
   curl -X PUT http://localhost:8080/api/users/profile \
     -H "Authorization: Bearer <access_token>" \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Updated Test User",
       "addresses": ["123 Test Street"]
     }'
   ```
   - Expected Response: 200 OK
   - Verify the profile was updated by making another GET request

5. **Refresh Access Token**
   ```bash
   curl -X POST http://localhost:8080/api/auth/refresh-token \
     -H "Cookie: refresh_token=<refresh_token>"
   ```
   - Expected Response: 200 OK with new access token
   - Save the new access token for subsequent requests

6. **Logout**
   ```bash
   curl -X POST http://localhost:8080/api/auth/logout \
     -H "Authorization: Bearer <access_token>"
   ```
   - Expected Response: 200 OK
   - Verify the token is invalidated by attempting to access a protected endpoint

### Postman Testing Flow

1. Import the Postman collection from `docs/postman/UserService1B.postman_collection.json`
2. Create a new environment in Postman with variables:
   - `base_url`: http://localhost:8080/api
   - `access_token`: (will be set after login)
   - `refresh_token`: (will be set after login)

3. Execute requests in this order:
   - Register User
   - Login
   - Get User Profile
   - Update User Profile
   - Refresh Token
   - Logout

4. After each successful request:
   - Check the response status code
   - Verify the response body
   - Update environment variables if new tokens are received

### Common Test Scenarios

1. **Invalid Registration**
   - Try registering with an existing email
   - Try registering with invalid password format
   - Try registering without required fields

2. **Invalid Login**
   - Try logging in with wrong password
   - Try logging in with non-existent email
   - Try logging in with invalid credentials format

3. **Token Validation**
   - Try accessing protected endpoints without token
   - Try accessing with expired token
   - Try accessing with invalid token format

4. **Authorization**
   - Try accessing admin endpoints with user role
   - Try accessing user endpoints with invalid role

### Testing Tips

1. Use different email addresses for each test run to avoid conflicts
2. Clear cookies between test runs
3. Check response headers for security configurations
4. Verify token expiration times
5. Test error scenarios with invalid inputs
6. Monitor MongoDB for data consistency

## Deployment

### Prerequisites
- Java 17
- MongoDB 4.4+
- Maven 3.8+

### Environment Variables
```bash
export MONGODB_URI=mongodb://localhost:27017/userservice
export JWT_SECRET_KEY=<base64_encoded_secret>
export JWT_REFRESH_SECRET_KEY=<base64_encoded_refresh_secret>
```

### Build
```bash
./mvnw clean package
```

### Run
```bash
java -jar target/userservice-1.0.0.jar
```

## Security

### JWT Configuration
- Access Token Expiration: 15 minutes
- Refresh Token Expiration: 7 days
- Algorithm: HS256

### Security Headers
- HSTS enabled
- XSS Protection
- Content Security Policy
- Frame Options

### CORS Configuration
```yaml
security:
  cors:
    allowed-origins:
      - http://localhost:3000
      - http://localhost:4200
    allowed-methods:
      - GET
      - POST
      - PUT
      - DELETE
      - OPTIONS
    allowed-headers:
      - Authorization
      - Content-Type
    max-age: 3600
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
