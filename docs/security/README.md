# Security Documentation

## Overview

This document provides a comprehensive overview of the security measures implemented in the UserService1B application, focusing on authentication, authorization, data protection, and best practices.

## Authentication

### JWT-Based Authentication

The application uses JSON Web Tokens (JWT) for authentication with the following components:

1. **Token Generation**: During login, two tokens are generated:
   - Access Token: Short-lived token for API access
   - Refresh Token: Long-lived token for obtaining new access tokens

2. **Token Structure**:
   ```
   Header: { "alg": "HS256", "typ": "JWT" }
   Payload: {
     "sub": "user_id",
     "roles": ["ROLE_USER"],
     "iat": 1625097600,
     "exp": 1625101200
   }
   Signature: HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
   ```

3. **Token Validation**: 
   - `JWTAuthenticationFilter` intercepts requests and validates tokens
   - JWT signature validation ensures token integrity
   - Expiration time check prevents use of expired tokens

### Refresh Token Management

Refresh tokens are managed using MongoDB for persistence:

1. **Storage**: Tokens are stored with user association and expiration
2. **Validation**: Whitelist approach - only tokens in the database are valid
3. **Revocation**: Tokens can be removed during logout
4. **Multi-Device Support**: Multiple tokens per user enable multiple sessions

## Authorization

### Role-Based Access Control

The application implements RBAC with the following roles:

1. **ROLE_USER**: Regular user with access to:
   - Own profile management
   - Basic application features

2. **ROLE_ADMIN**: Administrative user with access to:
   - All user management operations
   - System configuration
   - Administrative features

3. **Method-Level Security**:
   ```java
   @PreAuthorize("hasRole('ADMIN')")
   public List<UserDTO> getAllUsers() { ... }
   ```

4. **URL-Based Security**:
   ```java
   .requestMatchers("/api/admin/**").hasRole("ADMIN")
   .requestMatchers("/api/users/profile").authenticated()
   ```

## Security Headers

The application implements the following security headers:

```java
http.headers()
    .contentSecurityPolicy("default-src 'self'")
    .frameOptions().deny()
    .xssProtection()
    .and()
    .cacheControl();
```

1. **Content Security Policy**: Prevents XSS attacks
2. **X-Frame-Options**: Prevents clickjacking attacks
3. **X-XSS-Protection**: Additional XSS protection
4. **Cache Control**: Prevents sensitive data caching

## CORS Configuration

Cross-Origin Resource Sharing is configured to:

1. Allow specific origins defined in application properties
2. Support credentials for authenticated requests
3. Allow specific HTTP methods (GET, POST, PUT, DELETE)
4. Allow required headers for authentication

## Password Security

1. **Hashing**: BCrypt password encoder with adaptive work factor
2. **Validation**: Password complexity requirements:
   - Minimum length: 8 characters
   - At least one uppercase letter
   - At least one lowercase letter
   - At least one digit
   - At least one special character

## Data Protection

1. **Input Validation**: All input data is validated
2. **Output Encoding**: Data is properly encoded when returned to clients
3. **DTO Pattern**: Separates internal entities from external representation

## Security Best Practices

1. **Secrets Management**:
   - JWT secrets stored as environment variables
   - No hardcoded credentials in codebase

2. **Session Management**:
   - Short-lived access tokens
   - Secure token storage
   - Token refresh mechanism

3. **Error Handling**:
   - Generic error messages to users
   - Detailed logging for debugging
   - No sensitive information in error responses

4. **Logging**:
   - Authentication successes and failures
   - Access to sensitive operations
   - No sensitive data in logs

## Security Testing

1. **Unit Tests**: Test security components
2. **Integration Tests**: Test security in context
3. **Manual Security Review**: Regular code reviews

## Related Documents

- [Refresh Token Implementation](../refresh_token.md) - Detailed guide on refresh token implementation 