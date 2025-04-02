# JWT Authentication System Presentation

## Slide 1: Introduction
- **Title**: User Service with JWT Authentication
- **Subtitle**: Secure Microservice Authentication with Spring Boot & MongoDB
- **Author**: System Architect
- **Date**: April 2025

## Slide 2: System Overview
- Microservice architecture for e-commerce platform
- User Service responsible for authentication and user management
- JWT-based authentication system with refresh token mechanism
- Role-based access control (RBAC) for protected endpoints

## Slide 3: Technology Stack
- **Backend**: Spring Boot 3.4.3
- **Database**: MongoDB
- **Authentication**: JWT with Spring Security
- **Documentation**: OpenAPI (Swagger)
- **Build Tool**: Maven

## Slide 4: Authentication Flow Diagram
```
[CLIENT] -------Register/Login-------> [SERVER]
            <----JWT Tokens----------
       
[CLIENT] -------Access Token--------> [SERVER]
            <----Protected Data------

[CLIENT] -------Refresh Token-------> [SERVER]
            <----New Access Token----

[CLIENT] -------Logout/Revoke-------> [SERVER]
            <----Token Blacklisted---
```

## Slide 5: Database Schema
```
┌────────────────────────┐       ┌────────────────────────┐
│      UserEntity        │       │        Address         │
├────────────────────────┤       ├────────────────────────┤
│ id: String (ObjectId)  │       │ id: String (ObjectId)  │
│ name: String           │       │ street: String         │
│ email: String          │       │ city: String           │
│ password: String       │       │ state: String          │
│ roles: List<String>    │───────│ zipCode: String        │
│ addresses: List<Address>│       └────────────────────────┘
│ orderIds: List<String> │
└────────────────────────┘
```

## Slide 6: JWT Token Structure
- **Header**: Algorithm & token type
- **Payload**: Contains claims
  - Subject (email)
  - User ID
  - Roles
  - Issued at & expiration time
- **Signature**: Verifies token integrity

## Slide 7: Key Components
1. **Controllers**: Handle API requests
2. **Services**: Implement business logic
3. **JWT Components**: Token generation and validation
4. **Security Filters**: Authentication and authorization
5. **Token Blacklist**: Logout mechanism

## Slide 8: Authentication Methods
1. **Register**: Create new user account
2. **Login**: Generate access & refresh tokens
3. **Refresh**: Get new tokens without re-authentication
4. **Logout**: Blacklist current token 
5. **Logout All**: Invalidate all user sessions

## Slide 9: Security Features
- Password encryption with BCrypt
- Access token stored in memory (not localStorage)
- Refresh token in HTTP-only cookie
- Token blacklisting for revocation
- Role-based access control
- CORS configuration
- Input validation

## Slide 10: API Endpoints
- **/api/auth/user/register**: Create new user account
- **/api/auth/user/login**: Generate JWT tokens
- **/api/auth/refresh-token**: Refresh access token
- **/api/users/profile/{id}**: View user profile
- **/admin/user/all**: Manage all users (admin only)

## Slide 11: Token Storage Strategy
- **Access Token**: Short-lived, memory storage
- **Refresh Token**: Long-lived, HTTP-only cookie
- **Token Blacklist**: MongoDB/Redis storage

## Slide 12: Testing Approach
- Unit tests for services and controllers
- Integration tests for API endpoints
- Postman collection for manual testing
- Shell script for automated testing
- Stress testing for token management

## Slide 13: Sequence Diagram
```
User Registration → Authentication → Resource Access → Token Refresh → Logout
```
- Each step involves specific security checks
- System validates tokens on every protected request
- Expired tokens trigger refresh flow

## Slide 14: Future Enhancements
- Redis for token blacklist storage
- OAuth2 integration
- Multi-factor authentication
- Rate limiting
- Session management dashboard
- PKCE for public clients

## Slide 15: Q&A
- Questions about the implementation?
- Security considerations?
- Integration with other microservices? 