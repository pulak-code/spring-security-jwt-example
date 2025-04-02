# Setting Up Postman Collection for API Testing

## Overview
This guide will help you set up a Postman collection to test the User Service API endpoints.

## Steps

### 1. Create a New Collection
1. Open Postman
2. Click "Collections" in the sidebar
3. Click "+" to create a new collection
4. Name it "User Service API"

### 2. Set Up Environment Variables
1. Click "Environments" in the sidebar
2. Create a new environment named "Local Dev"
3. Add the following variables:
   - `base_url`: `http://localhost:8081`
   - `user_token`: (leave empty for now)
   - `admin_token`: (leave empty for now)
   - `user_id`: (leave empty for now)

### 3. Create Request Folders
Create the following folders in your collection:
1. Auth
2. User Management
3. Admin Management

### 4. Auth Requests

#### Register User
1. Create a POST request: `{{base_url}}/api/auth/user/register`
2. Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "email": "user123@example.com",
  "password": "User@123",
  "name": "Test User",
  "roles": ["ROLE_USER"]
}
```

#### Register Admin
1. Create a POST request: `{{base_url}}/api/auth/user/register`
2. Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "email": "admin123@example.com",
  "password": "Admin@123",
  "name": "Admin User",
  "roles": ["ROLE_ADMIN"]
}
```

#### User Login
1. Create a POST request: `{{base_url}}/api/auth/user/login`
2. Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "email": "user123@example.com",
  "password": "User@123"
}
```
4. Tests tab:
```javascript
var jsonData = pm.response.json();
if (jsonData.accessToken) {
    pm.environment.set("user_token", jsonData.accessToken);
}
```

#### Admin Login
1. Create a POST request: `{{base_url}}/api/auth/user/login`
2. Headers: `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "email": "admin123@example.com",
  "password": "Admin@123"
}
```
4. Tests tab:
```javascript
var jsonData = pm.response.json();
if (jsonData.accessToken) {
    pm.environment.set("admin_token", jsonData.accessToken);
}
```

#### Refresh Token
1. Create a POST request: `{{base_url}}/api/auth/refresh-token`
2. No headers or body needed (cookies are automatically included)

#### Logout
1. Create a POST request: `{{base_url}}/api/auth/logout`
2. Headers: `Authorization: Bearer {{user_token}}`

#### Logout All
1. Create a POST request: `{{base_url}}/api/auth/logout-all`
2. Headers: `Authorization: Bearer {{user_token}}`

### 5. User Management Requests

#### Get User Profile
1. Create a GET request: `{{base_url}}/api/users/profile/{{user_id}}`
2. Headers: `Authorization: Bearer {{user_token}}`

#### Edit User Details
1. Create a PATCH request: `{{base_url}}/api/users/user/{{user_id}}`
2. Headers: 
   - `Authorization: Bearer {{user_token}}`
   - `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "email": "updated_user123@example.com",
  "password": "UpdatedPass@123"
}
```

#### Delete User
1. Create a DELETE request: `{{base_url}}/api/users/users/{{user_email}}`
2. Headers: `Authorization: Bearer {{user_token}}`
3. Set `user_email` as a variable or replace with the actual email

### 6. Admin Management Requests

#### Get All Users
1. Create a GET request: `{{base_url}}/admin/user/all`
2. Headers: `Authorization: Bearer {{admin_token}}`
3. Tests tab:
```javascript
var jsonData = pm.response.json();
if (jsonData && jsonData.length > 0) {
    pm.environment.set("user_id", jsonData[0].id);
}
```

#### Search Users
1. Create a GET request: `{{base_url}}/admin/user/search?email=user123@example.com`
2. Headers: `Authorization: Bearer {{admin_token}}`

#### Update User as Admin
1. Create a PUT request: `{{base_url}}/admin/userupdate/{{user_id}}`
2. Headers: 
   - `Authorization: Bearer {{admin_token}}`
   - `Content-Type: application/json`
3. Body (raw JSON):
```json
{
  "name": "Updated By Admin",
  "roles": ["ROLE_USER"]
}
```

#### Admin Dashboard
1. Create a GET request: `{{base_url}}/admin/dashboard`
2. Headers: `Authorization: Bearer {{admin_token}}`

#### Delete All Users
1. Create a DELETE request: `{{base_url}}/admin/user/all`
2. Headers: `Authorization: Bearer {{admin_token}}`

### 7. Set Up Collection Runner

1. Click on the collection's "Run" button
2. Arrange the requests in the following order:
   - Register User
   - Register Admin
   - User Login
   - Admin Login
   - Get All Users
   - Get User Profile
   - Edit User Details
   - Admin Dashboard
   - Search Users
   - Update User as Admin
   - Logout
   - User Login (again)
   - Logout All
   - Delete User
   - Delete All Users

3. Configure delay between requests (e.g., 500ms)
4. Click "Run" to execute the entire sequence

## Validation

After running the collection:
1. Check that your environment variables are populated correctly
2. Review each request's response for expected status codes and content
3. Verify that the token blacklisting works by trying to use a logged-out token 