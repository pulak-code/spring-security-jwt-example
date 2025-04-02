# Security Implementation - Method Flow and Description

## Overview

This document describes the security implementation of the User Service API, focusing on the JWT authentication mechanism, token management, and authorization flow. The security package is structured into several components, each responsible for specific aspects of the authentication and authorization process.

## Package Structure

The security package is organized into the following sub-packages:

1. **config**: Contains security configuration classes
2. **filter**: Contains HTTP filters for authentication and CORS
3. **utilservice**: Contains utility services for JWT operations
4. **model**: Contains user details service implementation
5. **authprovider**: Contains authentication providers

## 1. Security Flow

![Authentication Flow](https://mermaid.ink/img/pako:eNqNkl1PwjAUhv9K057uJioXQhqjAaKAIGSiNxjoVlfXjnbtMhP_u10NRDciNtt5P55z3rPDSHFIKIuZhnOTWWhRb2GWl1wJRoQtELQgRqTMrDR9LBsQoQsjZ2_RKBpPLqkKF8YXzqXY3hDn2qJUxSfTBp2qyaOsbEyEkMxOuV8bR1DlNnwWQoKUqNpEcQ3_VJQjv1U9D5SJkNtVUbTNQiiXyG7cAzUUNtRdbXiZ2TGo-Q1fgXJlylfbaqvgcJxEVaJLpEHuRDDk8lkxlRjZNB9Xx6-TNNDT1aAyGQpQBd9R_u9cZkx0HK9OMj4Cg7xvvY7_GWfXz3m-qD4eZGXZK8QoSMKjDVvXeX95p9UdXE6OsxPn-9LvTf6RHtS7z_5e6_enr32n1X3Q6r2DONxzxmEpRNXP1QYKZImIWY4KH2LvBUJGC4VHq0cjZyOWmkP4EYUbHudw8x8swvOC7aKQsNhlQbj-BocVuRU?type=png)

### Authentication Process:

1. The client sends a request with JWT in the Authorization header
2. The `JWTAuthenticationFilter` intercepts the request and extracts the token
3. The filter uses `JWTUtil` to validate the token and extract user information
4. If the token is valid and not blacklisted, the user is authenticated
5. The authenticated user is added to the SecurityContext
6. The request proceeds to the controller

## 2. Class and Method Descriptions

### 2.1 SecurityConfig

**Purpose**: Configures Spring Security settings, including authentication providers, password encoding, and request authorization rules.

**Key Methods**:
- `securityFilterChain(HttpSecurity http)`: Configures security rules for different endpoints
- `userDetailsService(UserRepository userRepo)`: Creates a custom UserDetailsService bean
- `authenticationManager(UserDetailsService, PasswordEncoder)`: Creates an authentication manager with DAO and JWT providers
- `passwordEncoder()`: Creates a BCrypt password encoder
- `corsFilter()`: Creates a CORS filter bean

### 2.2 JWTUtil

**Purpose**: Provides utilities for JWT token generation, validation, and processing.

**Key Methods**:
- `generateAccessToken(UserEntity user)`: Creates a new JWT access token with user claims
- `generateRefreshToken(String userEmail)`: Creates a new refresh token
- `generateRotatedRefreshToken(String oldRefreshToken, TokenBlacklistService)`: Blacklists old token and generates a new one
- `validateToken(String token)`: Validates token signature, expiry, and user existence
- `extractUser(String token)`: Extracts user details from token
- `parseToken(String token)`: Parses and verifies token signature
- `getPayload(String token)`: Extracts claims from token
- `preProcessingTokenChecks(Optional<String> authHeader)`: Extracts and validates token format from Authorization header

**Configuration Properties**:
- `jwt.secret.key`: The secret key used for JWT token signing
- `security.jwt.expiration-ms`: Access token expiration time in milliseconds (default: 2400000 - 40 minutes)
- `security.jwt.refresh-expiration-ms`: Refresh token expiration time in milliseconds (default: 86400000 - 24 hours)

### 2.3 JWTAuthenticationFilter

**Purpose**: Intercepts HTTP requests to authenticate users via JWT tokens.

**Key Methods**:
- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)`: Intercepts requests and extracts tokens
- `processToken(String token)`: Validates token, checks blacklist, and authenticates user

### 2.4 TokenBlacklistService

**Purpose**: Manages blacklisted JWT tokens to prevent their reuse after logout.

**Key Methods**:
- `isTokenBlacklisted(String token)`: Checks if a token is in the blacklist
- `blackListToken(String token, Long expiryTime)`: Adds a token to the blacklist
- `removeExpiredTokens()`: Scheduled task to clear expired tokens from blacklist

### 2.5 TokenStorageService

**Purpose**: Manages storage and retrieval of JWT tokens with multiple storage strategies.

**Key Methods**:
- `storeAccessToken(String token)`: Stores access token based on strategy
- `storeRefreshToken(String token)`: Stores refresh token based on strategy
- `getAccessToken()`: Retrieves stored access token
- `getRefreshToken()`: Retrieves stored refresh token
- `clearTokens()`: Clears stored tokens
- `setStorageStrategy(StorageStrategy)`: Sets token storage mechanism (MEMORY, ENVIRONMENT, PROPERTIES)

**Configuration Properties**:
- `token.storage.strategy`: The strategy to use for token storage (MEMORY, ENVIRONMENT, PROPERTIES)

### 2.6 CustomUserDetailsService

**Purpose**: Loads user-specific data from the database for authentication.

**Key Methods**:
- `loadUserByUsername(String email)`: Loads user details from database and converts to Spring Security UserDetails

### 2.7 JwtAuthenticationProvider

**Purpose**: Custom authentication provider that validates JWT tokens.

**Key Methods**:
- `authenticate(Authentication authentication)`: Authenticates users based on JWT token
- `supports(Class<?> authentication)`: Determines if provider supports the authentication type

### 2.8 CorsFilter

**Purpose**: Handles Cross-Origin Resource Sharing (CORS) for the API.

**Key Methods**:
- `doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)`: Adds CORS headers to responses

## 3. Security Mechanisms

### 3.1 Token Validation

The system validates tokens in multiple layers:
1. The `JWTAuthenticationFilter` intercepts all requests and checks for JWT tokens
2. The `JWTUtil.validateToken()` method verifies:
   - Token signature validity
   - Token expiration time
   - User existence in the database
3. The `TokenBlacklistService` ensures revoked tokens cannot be reused

### 3.2 Token Blacklisting

When a user logs out:
1. The token is added to the blacklist in `TokenBlacklistService`
2. The token remains in the blacklist until its original expiration time
3. A scheduled task cleans up expired tokens to prevent memory leaks

### 3.3 Password Security

User passwords are secured using:
1. BCrypt hashing with a strength factor of 9
2. Password encoding happens during user registration
3. Password validation happens during authentication

### 3.4 Role-Based Authorization

The system implements role-based access control:
1. User roles are stored in the database
2. Roles are included in the JWT token
3. Spring Security's `hasRole()` method restricts access to endpoints based on roles

## 4. Best Practices Implemented

1. **JWT Security**:
   - Short-lived access tokens
   - Refresh token rotation
   - Token blacklisting
   - Signing key protection

2. **API Security**:
   - CORS protection
   - CSRF disabled for stateless APIs
   - Role-based access control
   - Secure password hashing

3. **Error Handling**:
   - Proper exception handling
   - Detailed logging without sensitive information
   - User-friendly error messages

## 5. Future Enhancements

1. Replace in-memory token blacklist with:
   - Redis for distributed deployments
   - MongoDB with TTL indexes for persistence

2. Implement additional security features:
   - Rate limiting
   - Brute force protection
   - IP-based restrictions

3. Enhance token management:
   - Token revocation by user ID
   - Device-based token tracking 