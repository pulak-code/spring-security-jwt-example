# User Service System Architecture

## Overview
This document provides a comprehensive overview of the User Service architecture, including sequence diagrams, database schema, and authentication flows.

## Authentication Flow

```
┌─────────┐          ┌─────────────┐          ┌─────────────┐          ┌──────────┐
│  Client  │          │  AuthController │          │  AuthService  │          │ Database │
└────┬────┘          └───────┬─────┘          └───────┬─────┘          └────┬─────┘
     │                       │                        │                      │
     │  Register Request     │                        │                      │
     │─────────────────────>│                        │                      │
     │                       │                        │                      │
     │                       │  registerUser()        │                      │
     │                       │───────────────────────>│                      │
     │                       │                        │  Save User           │
     │                       │                        │─────────────────────>│
     │                       │                        │  User Saved          │
     │                       │                        │<─────────────────────│
     │  201 Created          │                        │                      │
     │<─────────────────────│<───────────────────────│                      │
     │                       │                        │                      │
     │  Login Request        │                        │                      │
     │─────────────────────>│                        │                      │
     │                       │  authenticateUser()    │                      │
     │                       │───────────────────────>│                      │
     │                       │                        │  Find User           │
     │                       │                        │─────────────────────>│
     │                       │                        │  User Found          │
     │                       │                        │<─────────────────────│
     │                       │                        │                      │
     │                       │                        │  Generate JWT        │
     │                       │                        │←──────────────┐     │
     │                       │                        │               │     │
     │                       │                        │  Store Refresh Token │
     │                       │                        │─────────────────────>│
     │  200 OK + Tokens      │                        │                      │
     │<─────────────────────│<───────────────────────│                      │
     │                       │                        │                      │
     │  Protected Request    │                        │                      │
     │  + Access Token       │                        │                      │
     │─────────────────────>│                        │                      │
     │                       │  JWT Filter Validates  │                      │
     │                       │←──────────────┐       │                      │
     │                       │               │       │                      │
     │  200 OK + Resource    │               │       │                      │
     │<─────────────────────│               │       │                      │
     │                       │               │       │                      │
     │  Token Refresh Request│               │       │                      │
     │  + Refresh Token      │               │       │                      │
     │─────────────────────>│               │       │                      │
     │                       │  refreshToken()       │                      │
     │                       │───────────────────────>│                      │
     │                       │                        │  Validate Token      │
     │                       │                        │─────────────────────>│
     │                       │                        │  Token Valid         │
     │                       │                        │<─────────────────────│
     │                       │                        │                      │
     │                       │                        │  Generate New Tokens │
     │                       │                        │←──────────────┐     │
     │                       │                        │               │     │
     │  200 OK + New Tokens  │                        │                      │
     │<─────────────────────│<───────────────────────│                      │
     │                       │                        │                      │
     │  Logout Request       │                        │                      │
     │  + Access Token       │                        │                      │
     │─────────────────────>│                        │                      │
     │                       │  logout()              │                      │
     │                       │───────────────────────>│                      │
     │                       │                        │  Blacklist Token     │
     │                       │                        │─────────────────────>│
     │                       │                        │  Token Blacklisted   │
     │                       │                        │<─────────────────────│
     │  200 OK               │                        │                      │
     │<─────────────────────│<───────────────────────│                      │
     │                       │                        │                      │
```

## Database Schema

```
┌────────────────────────┐
│      UserEntity        │
├────────────────────────┤
│ id: String (ObjectId)  │
│ name: String           │
│ email: String          │
│ password: String       │
│ roles: List<String>    │
│ addresses: List<Address>│
│ orderIds: List<String> │
└────────────────────────┘
          │
          │
          ▼
┌────────────────────────┐
│        Address         │
├────────────────────────┤
│ id: String (ObjectId)  │
│ street: String         │
│ city: String           │
│ state: String          │
│ zipCode: String        │
└────────────────────────┘
```

## JWT Token Structure

```
┌─────────────────────────────────────────────┐
│              JWT Token Structure            │
├─────────────────────────────────────────────┤
│ Header:                                     │
│   {                                         │
│     "alg": "HS512",                         │
│     "typ": "JWT"                            │
│   }                                         │
├─────────────────────────────────────────────┤
│ Payload:                                    │
│   {                                         │
│     "sub": "user@example.com",              │
│     "roles": ["ROLE_USER"],                 │
│     "userId": "67ec3ad42b97ca685e3686d6",   │
│     "iat": 1743534862,                      │
│     "exp": 1743536062                       │
│   }                                         │
├─────────────────────────────────────────────┤
│ Signature: HMAC-SHA512(base64UrlEncode(     │
│   header) + "." + base64UrlEncode(payload), │
│   secret)                                   │
└─────────────────────────────────────────────┘
```

## System Components

1. **Controllers**:
   - **AuthController**: Handles authentication operations (register, login, refresh, logout)
   - **UsersController**: Manages user profile operations
   - **AdminController**: Provides admin-specific endpoints for user management

2. **Services**:
   - **AuthService**: Implements authentication and token management logic
   - **UserService**: Manages user profile operations
   - **AdminService**: Implements admin functionality
   - **TokenBlacklistService**: Handles token invalidation during logout

3. **Security Components**:
   - **JwtTokenProvider**: Generates and validates JWT tokens
   - **JwtAuthenticationFilter**: Filters and secures requests based on JWT presence
   - **UserDetailsService**: Loads user data for authentication

4. **Repositories**:
   - **UserRepository**: MongoDB data access for User documents

## Authentication Flow Description

1. **User Registration**:
   - Client sends registration request with email, password, name, and roles
   - Server validates request and checks if user exists
   - If valid, server creates new user with encrypted password
   - Server returns success message

2. **User Authentication**:
   - Client sends login request with email and password
   - Server validates credentials
   - If valid, server generates access token and refresh token
   - Server returns access token in response body and stores refresh token in HTTP-only cookie
   - Front-end stores access token in memory or secure storage

3. **Protected Resource Access**:
   - Client includes access token in Authorization header
   - Server validates token using JwtAuthenticationFilter
   - If valid, server processes the request and returns response
   - If invalid/expired, server returns 401 Unauthorized

4. **Token Refresh**:
   - When access token expires, client sends refresh request with refresh token
   - Server validates refresh token and checks if it's blacklisted
   - If valid, server generates new access and refresh tokens
   - Server returns new access token and updates refresh token cookie

5. **Logout**:
   - Client sends logout request with access token
   - Server adds token to blacklist to invalidate it
   - Server optionally clears refresh token cookie
   - Further requests with blacklisted token are rejected

6. **Logout All Devices**:
   - Similar to regular logout but invalidates all refresh tokens for the user
   - Requires all devices to re-authenticate

## Role-Based Access Control

The system uses Spring Security with JWT to implement role-based access control:

1. **ROLE_USER**: Regular users with access to:
   - Their own profile
   - Basic application features
   - Self-service profile management

2. **ROLE_ADMIN**: Administrators with access to:
   - All user profiles
   - User management features (search, update, delete)
   - System administration features

## Token Storage Strategy

1. **Access Token**: 
   - Short-lived (typically 20 minutes)
   - Stored in memory on client-side (not in local/session storage)
   - Included in Authorization header for API requests

2. **Refresh Token**:
   - Long-lived (typically 7-30 days)
   - Stored in HTTP-only secure cookie
   - Used to obtain new access tokens when needed 