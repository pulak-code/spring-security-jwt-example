# Security Documentation - UserService1B

## Security Features Overview

1. **Authentication & JWT Management**
   - JWT-based authentication
   - Token management & refresh mechanism
   - Role-based access control

2. **Security Features**
   - Account lockout mechanism
   - Progressive delay implementation
   - CORS security
   - Security headers
   - Security metrics & monitoring

3. **Validation & Protection**
   - Input validation
   - Password policy
   - Security property validation
   - Error handling

## 1. Authentication & Authorization

### JWT-based Authentication
- **Implementation**: Uses JSON Web Tokens (JWT) for stateless authentication
- **Token Structure**:
  ```
  Header: {
    "alg": "HS256",
    "typ": "JWT"
  }
  Payload: {
    "sub": "<user_id>",
    "roles": ["ROLE_USER"],
    "iat": <timestamp>,
    "exp": <timestamp>
  }
  ```
- **Token Lifecycle**:
  - Access Token: 24 hours (configurable)
  - Refresh Token: 7 days (configurable)

### Token Management
1. **Access Token**:
   - Generated on successful authentication
   - Required for all protected endpoints
   - Validated on each request

2. **Refresh Token**:
   - Used to obtain new access tokens
   - Invalidated on password change
   - Rotation policy implemented

3. **Role-based Access Control**:
   - Supported roles: USER, ADMIN
   - Path-based authorization:
     ```java
     .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
     .requestMatchers("/api/v1/user/**").hasRole("USER")
     ```

## 2. Security Features

### Account Lockout Mechanism

The service implements a sophisticated account lockout system using JPA entities and repositories:

```java
@Repository
public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long> {
    Optional<FailedLoginAttempt> findByEmail(String email);
    List<FailedLoginAttempt> findExpiredLockouts(LocalDateTime now);
    void resetAttempts(String email, LocalDateTime now);
    boolean isAccountLocked(String email, LocalDateTime now);
}
```

1. **Account Lockout Features**:
   - Tracks failed login attempts per email
   - Implements automatic lockout after maximum attempts
   - Maintains lockout duration with automatic expiry
   - Provides methods to check and reset lockout status

2. **Progressive Delay Implementation**:
   - Implements increasing delays between login attempts
   - Prevents brute force attacks
   - Configurable delay parameters
   - Automatic reset after successful login

3. **Security Validations**:
   - Validates attempt counts against maximum allowed
   - Checks lockout status before authentication
   - Enforces progressive delays between attempts
   - Automatically expires lockouts after duration

### CORS Security Implementation

```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityCorsFilter extends OncePerRequestFilter {
    // Validates and applies CORS headers before security processing
    // Handles preflight requests explicitly
    // Logs all CORS operations for audit
}
```

### Security Validation Pipeline

```java
@Component
public class SecurityPropertiesValidator {
       // Validates CORS configurations
       private void validateCorsConfiguration() {
           // Checks for wildcard origins
           // Validates HTTPS requirements
           // Logs security warnings
       }

       // Validates authentication settings
       private void validateAuthConfiguration() {
           // Validates token expirations
           // Checks HTTPS requirements
           // Validates security protocols
       }

       // Validates security headers
       private void validateHeadersConfiguration() {
           // Validates HSTS settings
           // Checks frame options
           // Validates XSS protection
       }

       // Validates lockout settings
       private void validateLockoutConfiguration() {
           // Validates attempt limits
           // Checks lockout durations
           // Validates progressive delay
       }
   }
   ```
## 3. Security Validations

### Input Validation
1. **Bean Validation**:
   - @NotNull, @NotEmpty, @Size
   - @Pattern for format validation
   - Custom validators for complex rules

2. **Password Policy**:
   ```java
   @Pattern(
     regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
     message = "Password must be 8+ characters, containing digits, lower, upper, special chars"
   )
   ```

3. **Token Validation**:
   - Signature verification
   - Expiration check
   - Role validation
   - Token blacklist check

4. **Security Metrics**:
   ```java
   // Authentication metrics tracked via MeterRegistry
   Counter authSuccessCounter = Counter.builder("security.authentication")
       .description("Number of successful authentication attempts")
       .tag("result", "success")
       .register(meterRegistry);

   Counter authFailureCounter = Counter.builder("security.authentication")
       .description("Number of failed authentication attempts")
       .tag("result", "failure")
       .register(meterRegistry);
   ```

## 4. Configuration Guide

### Implemented Security Best Practices

1. **JWT Security**:
   - Tokens stored in HttpOnly cookies
   - Token rotation with refresh mechanism
   - Short-lived access tokens (24h max)
   - Refresh tokens with 7-day maximum lifetime
   - Automatic token invalidation on security events

2. **Request Security**:
   - All endpoints protected by default
   - Strong CORS policy enforcement
   - Security headers automatically applied:
     ```java
     .headers(headers -> headers
         .frameOptions(frame -> frame.deny())
         .xssProtection(xss -> xss.enable())
         .contentSecurityPolicy(csp ->
             csp.policyDirectives("default-src 'self'; frame-ancestors 'none';"))
         .httpStrictTransportSecurity(hsts ->
             hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
         .referrerPolicy(referrer -> referrer.strictOrigin())
     )
     ```

3. **Authentication Security**:
   - BCrypt password hashing (strength = 12)
   - Progressive lockout mechanism
   - Secure password validation
   - Authentication attempt tracking

4. **Monitoring & Auditing**:
   - Authentication success/failure metrics
   - Security event logging
   - Configuration validation warnings
   - Automatic security checks at startup
### Complete Security Configuration Reference

```yaml
security:
  # Authentication configuration
  auth:
    tokenExpiration: 24h
    refreshTokenExpiration: 7d
    requireHttps: true
  
  # CORS configuration
  cors:
    allowedOrigins:
      - "https://api.yourdomain.com"
    allowedMethods:
      - "GET"
      - "POST"
      - "PUT"
      - "DELETE"
      - "OPTIONS"
    allowCredentials: true
    maxAge: 3600
  
  # Security headers
  headers:
    enableHsts: true
    hstsMaxAge: 31536000
    hstsIncludeSubDomains: true
    contentSecurityPolicy: "default-src 'self'; frame-ancestors 'none';"
    referrerPolicy: "strict-origin"
    enableXssProtection: true
    denyFrameOptions: true
  
  # Account lockout
  lockout:
    maxAttempts: 5
    duration: 15m
    enableProgressiveDelay: true
    initialDelay: 1s
    maxDelay: 5m

  # Environment-specific overrides
  environments:
    development:
      requireHttps: false
      cors:
        allowedOrigins:
          - "http://localhost:3000"
          - "http://localhost:4200"
    
    production:
      requireHttps: true
      cors:
        allowedOrigins:
          - "https://api.yourdomain.com"
      headers:
        enableHsts: true
```
