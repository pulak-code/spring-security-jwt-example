# Architecture Documentation

## System Architecture

The UserService1B is designed as a microservice focusing on user management and authentication. It follows the principles of a layered architecture with clear separation of concerns.

```mermaid
graph TD
    Client[Client Applications] -->|HTTP Requests| API[API Layer/Controllers]
    API -->|Delegates| Service[Service Layer]
    Service -->|Data Ops| Repository[Repository Layer]
    Repository -->|Stores/Retrieves| MongoDB[MongoDB Database]
    Service -->|Auth| Security[Security Components]
    Security -->|JWT/Auth| TokenUtils[JWT/Token Utilities]
    Security -->|User Details| UserDetailsService[Custom UserDetailsService]
    TokenUtils -->|Token Store| MongoDB
```

## Component Structure

### Controller Layer
- **AuthController**: Handles authentication requests (login, register, refresh token)
- **UserController**: Handles user profile operations
- **AdminController**: Handles admin-specific operations

### Service Layer
- **AuthenticationService**: Manages authentication processes
- **UserService**: Handles user operations
- **AdminService**: Manages admin operations
- **RefreshTokenService**: Manages refresh token operations

### Repository Layer
- **UserRepository**: Data access for user entities
- **RefreshTokenRepository**: Data access for refresh tokens

### Security Components
- **SecurityConfig**: Spring Security configuration
- **JWTAuthFilter**: JWT authentication filter
- **JWTUtil**: JWT token creation and validation
- **CustomUserDetailsService**: Loads user details for authentication

## Data Flow

### Authentication Flow
```mermaid
sequenceDiagram
    participant User
    participant AuthController
    participant AuthService
    participant JWTUtil
    participant UserRepo
    participant TokenRepo
    
    User->>AuthController: Login Request
    AuthController->>AuthService: Authenticate
    AuthService->>UserRepo: Find User
    UserRepo-->>AuthService: User Entity
    AuthService->>JWTUtil: Generate Tokens
    JWTUtil-->>AuthService: Access + Refresh Tokens
    AuthService->>TokenRepo: Store Refresh Token
    AuthService-->>AuthController: Auth Response
    AuthController-->>User: Tokens + User Info
```

### Request Processing Flow
```mermaid
sequenceDiagram
    participant Client
    participant Filter
    participant Controller
    participant Service
    participant Repository
    
    Client->>Filter: HTTP Request + JWT
    Filter->>Filter: Validate JWT
    Filter-->>Controller: Authenticated Request
    Controller->>Service: Process Business Logic
    Service->>Repository: Data Operations
    Repository-->>Service: Data
    Service-->>Controller: Result
    Controller-->>Client: HTTP Response
```

## Technology Stack

- **Java 17**: Core programming language
- **Spring Boot 3.x**: Application framework
- **Spring Security**: Security framework
- **MongoDB**: NoSQL database
- **JWT**: Authentication mechanism
- **Maven**: Build tool

## Design Patterns

- **Repository Pattern**: For data access abstraction
- **Service Layer Pattern**: For business logic encapsulation
- **DTO Pattern**: For data transfer between layers
- **Builder Pattern**: For complex object construction
- **Factory Pattern**: For object creation
- **Strategy Pattern**: For various authentication mechanisms 