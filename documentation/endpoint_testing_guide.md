# User Service API Testing Guide

## Overview
This document provides a step-by-step guide for testing all endpoints in the User Service API, including authentication flow with access tokens, refresh tokens, and token blacklisting for logout functionality.

## Prerequisites
- The application is running on `http://localhost:8081`
- You have `curl` installed for API testing
- Optionally: Postman or another API client for easier testing

## Authentication Flow Testing

### Step 1: Register a Regular User
```bash
curl -X POST "http://localhost:8081/api/auth/user/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"user123@example.com","password":"User@123","name":"Test User","roles":["ROLE_USER"]}'
```
Expected response: 201 Created with message indicating successful registration

### Step 2: Register an Admin User
```bash
curl -X POST "http://localhost:8081/api/auth/user/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin123@example.com","password":"Admin@123","name":"Admin User","roles":["ROLE_ADMIN"]}'
```
Expected response: 201 Created with message indicating successful registration

### Step 3: Login as Regular User
```bash
curl -X POST "http://localhost:8081/api/auth/user/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user123@example.com","password":"User@123"}'
```
Expected response: 200 OK with access token in response body
**Important**: Save the access token as `USER_TOKEN` for subsequent requests

### Step 4: Login as Admin User
```bash
curl -X POST "http://localhost:8081/api/auth/user/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin123@example.com","password":"Admin@123"}'
```
Expected response: 200 OK with access token in response body
**Important**: Save the access token as `ADMIN_TOKEN` for subsequent requests
**Note**: The refresh token is automatically stored in an HTTP-only cookie

## User Endpoints Testing

### Step 5: Get User Profile (as User)
```bash
curl -X GET "http://localhost:8081/api/users/profile/{USER_ID}" \
  -H "Authorization: Bearer ${USER_TOKEN}"
```
Replace `{USER_ID}` with the actual user ID.
Expected response: 200 OK with user profile data

### Step 6: Edit User Details
```bash
curl -X PATCH "http://localhost:8081/api/users/user/{USER_ID}" \
  -H "Authorization: Bearer ${USER_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"email":"updated_user123@example.com","password":"UpdatedPass@123"}'
```
Replace `{USER_ID}` with the actual user ID.
Expected response: 200 OK with updated user data

## Admin Endpoints Testing

### Step 7: Access Admin Dashboard
```bash
curl -X GET "http://localhost:8081/admin/dashboard" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}"
```
Expected response: 200 OK with admin dashboard welcome message

### Step 8: Get All Users (Admin Only)
```bash
curl -X GET "http://localhost:8081/admin/user/all" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}"
```
Expected response: 200 OK with list of all users

### Step 9: Search Users by Email (Admin Only)
```bash
curl -X GET "http://localhost:8081/admin/user/search?email=user123@example.com" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}"
```
Expected response: 200 OK with matching users

**Note**: The search endpoint has been improved to handle null values gracefully. You can now:
- Search with just an email: `?email=user123@example.com`
- Search with just a name: `?username=admin`
- Search with both parameters: `?email=user&username=admin`
- Search with empty parameters: Will return all users if no criteria are provided

### Step 10: Update User as Admin
```bash
curl -X PUT "http://localhost:8081/admin/userupdate/{USER_ID}" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated By Admin","roles":["ROLE_USER"]}'
```
Replace `{USER_ID}` with the actual user ID.
Expected response: 200 OK with updated user data

## Refresh Token Testing

### Step 11: Refresh Access Token
```bash
curl -X POST "http://localhost:8081/api/auth/refresh-token" \
  -b "refreshToken=YOUR_REFRESH_TOKEN"
```
**Note**: The refresh token should be automatically included in the cookie from the login request.
Expected response: 200 OK with a new access token

## Logout Testing

### Step 12: Logout (Single Session)
```bash
curl -X POST "http://localhost:8081/api/auth/logout" \
  -H "Authorization: Bearer ${USER_TOKEN}"
```
Expected response: 200 OK with "Logged out successfully" message

### Step 13: Verify Token Blacklisting
Try to access a protected endpoint using the same token:
```bash
curl -X GET "http://localhost:8081/api/users/profile/{USER_ID}" \
  -H "Authorization: Bearer ${USER_TOKEN}"
```
Expected response: 401 Unauthorized (token has been blacklisted)

### Step 14: Logout from All Devices
First, login again to get a new token:
```bash
curl -X POST "http://localhost:8081/api/auth/user/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user123@example.com","password":"User@123"}'
```

Then logout from all devices using one of these methods:

#### a) Using Refresh Token Header (Recommended)
```bash
curl -X POST "http://localhost:8081/api/auth/logout-all" \
  -H "Refresh-Token: YOUR_REFRESH_TOKEN"
```

#### b) Using URL Parameter
```bash
curl -X POST "http://localhost:8081/api/auth/logout-all?refreshToken=YOUR_REFRESH_TOKEN"
```

#### c) Using Cookie (Automatic if browser)
```bash
curl -X POST "http://localhost:8081/api/auth/logout-all" \
  -b "refreshToken=YOUR_REFRESH_TOKEN"
```

Expected response: 200 OK with "Logged out from all devices" message

## Delete User Testing

### Step 15: Delete User (as Admin)
```bash
curl -X DELETE "http://localhost:8081/api/users/users/{USER_EMAIL}" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}"
```
Replace `{USER_EMAIL}` with the user's email.
Expected response: 200 OK with message indicating successful deletion

### Step 16: Delete All Users Except Current Admin (Admin Only)
```bash
curl -X DELETE "http://localhost:8081/admin/user/all" \
  -H "Authorization: Bearer ${ADMIN_TOKEN}"
```
Expected response: 200 OK with message confirming deletion

## Common Issues and Troubleshooting

1. **403 Forbidden**: Check if the user has the appropriate role (ROLE_USER or ROLE_ADMIN)
2. **401 Unauthorized**: 
   - Verify that the token is valid and hasn't expired
   - Make sure the token isn't blacklisted
   - Check that you're using the correct format: "Authorization: Bearer YOUR_TOKEN"
3. **500 Internal Server Error**: 
   - Check application logs for details
   - Verify MongoDB connection
4. **Token not being saved in cookie**: 
   - Check browser settings to allow cookies
   - Ensure the application is setting the cookie properly
5. **Search by Email returning error**:
   - This issue has been fixed. The search endpoint now handles null values properly.
6. **Logout from All Devices failing**:
   - This issue has been fixed. You can now provide the refresh token in multiple ways.
   - If you get "Invalid refresh token" error, make sure you're using a valid JWT token. 