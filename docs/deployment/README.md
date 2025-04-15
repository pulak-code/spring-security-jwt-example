# Deployment Guide

## Prerequisites

- Java 17 or higher
- MongoDB (4.4+)
- Maven 3.6+
- Docker (optional, for containerized deployment)
- Kubernetes (optional, for orchestrated deployment)

## Environment Setup

### Configuration Properties

The application uses the following environment variables for configuration:

```
# Server Configuration
SERVER_PORT=8081

# MongoDB Configuration
MONGODB_URI=mongodb://localhost:27017/UserDb
MONGODB_DATABASE=UserDb

# JWT Configuration
JWT_SECRET_KEY=YOUR_BASE64_ENCODED_SECRET_KEY
JWT_EXPIRATION_MS=3600000
JWT_REFRESH_EXPIRATION_MS=604800000

# Security Configuration
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
SECURITY_HEADERS_ENABLED=true
```

### Local Deployment

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/userservice1b.git
   cd userservice1b
   ```

2. **Set environment variables**:
   ```bash
   export JWT_SECRET_KEY=$(openssl rand -base64 64)
   export JWT_EXPIRATION_MS=3600000
   export JWT_REFRESH_EXPIRATION_MS=604800000
   ```

3. **Build the application**:
   ```bash
   ./mvnw clean package -DskipTests
   ```

4. **Run the application**:
   ```bash
   java -jar target/userservice1b-0.0.1-SNAPSHOT.jar
   ```

### Docker Deployment

1. **Build Docker image**:
   ```bash
   docker build -t userservice1b:latest .
   ```

2. **Run Docker container**:
   ```bash
   docker run -d \
     --name userservice1b \
     -p 8081:8081 \
     -e MONGODB_URI=mongodb://mongo:27017/UserDb \
     -e JWT_SECRET_KEY=YOUR_BASE64_ENCODED_SECRET_KEY \
     -e JWT_EXPIRATION_MS=3600000 \
     -e JWT_REFRESH_EXPIRATION_MS=604800000 \
     -e CORS_ALLOWED_ORIGINS=http://localhost:3000 \
     userservice1b:latest
   ```

### Docker Compose Deployment

1. **Create a docker-compose.yml file**:
   ```yaml
   version: '3'
   services:
     app:
       image: userservice1b:latest
       ports:
         - "8081:8081"
       environment:
         - MONGODB_URI=mongodb://mongo:27017/UserDb
         - JWT_SECRET_KEY=YOUR_BASE64_ENCODED_SECRET_KEY
         - JWT_EXPIRATION_MS=3600000
         - JWT_REFRESH_EXPIRATION_MS=604800000
         - CORS_ALLOWED_ORIGINS=http://localhost:3000
       depends_on:
         - mongo
     
     mongo:
       image: mongo:4.4
       ports:
         - "27017:27017"
       volumes:
         - mongodb_data:/data/db

   volumes:
     mongodb_data:
   ```

2. **Run with Docker Compose**:
   ```bash
   docker-compose up -d
   ```

## Production Deployment Considerations

### Security

- Use a secrets manager for sensitive values
- Rotate JWT keys regularly
- Enable TLS/SSL for all communications
- Use non-root users in containers

### Scalability

- Configure horizontal scaling for the application
- Set up MongoDB replica sets
- Use a load balancer for distributing traffic

### Monitoring

- Implement health checks
- Set up logging aggregation
- Configure monitoring for performance metrics

### Backup and Recovery

- Implement MongoDB backup strategy
- Document recovery procedures
- Test backup restoration regularly

## Deployment Verification

After deployment, verify the service is running correctly:

```bash
# Check service health
curl http://localhost:8081/actuator/health

# Verify API access
curl http://localhost:8081/api/auth/status
``` 