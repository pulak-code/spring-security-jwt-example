# API Documentation

## Overview
This section contains documentation for all API endpoints provided by the UserService1B application.

## Authentication Endpoints

### User Authentication

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/user/register` | POST | Register a new user |
| `/api/auth/user/login` | POST | Login as a user and receive tokens |
| `/api/auth/refresh-token` | POST | Refresh access token using refresh token |
| `/api/auth/logout` | POST | Logout (invalidate current token) |
| `/api/auth/logout-all` | POST | Logout from all devices |

### Admin Authentication

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/admin/register` | POST | Register a new admin (protected) |
| `/api/auth/admin/login` | POST | Login as an admin and receive tokens |

## User Management Endpoints

### User Operations

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/users/profile` | GET | Get current user profile |
| `/api/users/profile` | PUT | Update current user profile |
| `/api/users/{id}` | GET | Get user by ID (Admin only) |
| `/api/users/email/{email}` | GET | Get user by email (Admin only) |

### Admin Operations

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/admin/users` | GET | Get all users (Admin only) |
| `/api/admin/users/{id}` | DELETE | Delete user by ID (Admin only) |
| `/api/admin/users/{id}/disable` | PUT | Disable user account (Admin only) |
| `/api/admin/users/{id}/enable` | PUT | Enable user account (Admin only) |

## Request/Response Examples

See the [Postman collection](../postman/UserService1B.postman_collection.json) for detailed request/response examples.

## Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | OK - Request succeeded |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Invalid input parameters |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource already exists |
| 500 | Internal Server Error - Server-side error | 