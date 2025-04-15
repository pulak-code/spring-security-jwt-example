package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.config.SecurityProperties;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JWTUtil {
    private static final Logger jwtUtilLogger = LoggerFactory.getLogger(JWTUtil.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();
    
    private final UserService userService;
    private final SecurityProperties securityProperties;
    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    public JWTUtil(@Lazy UserService userService, SecurityProperties securityProperties) {
        this.userService = userService;
        this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void init() {
        try {
            // Initialize access token key
            String accessSecret = securityProperties.getAuth().getSecretKey();
            if (accessSecret == null || accessSecret.isBlank()) {
                throw new IllegalStateException("Access token secret key is not configured");
            }
            byte[] decodedAccessKey = Base64.getDecoder().decode(accessSecret);
            this.accessTokenKey = Keys.hmacShaKeyFor(decodedAccessKey);
            
            // Initialize refresh token key
            String refreshSecret = securityProperties.getAuth().getRefreshSecretKey();
            if (refreshSecret == null || refreshSecret.isBlank()) {
                throw new IllegalStateException("Refresh token secret key is not configured");
            }
            byte[] decodedRefreshKey = Base64.getDecoder().decode(refreshSecret);
            this.refreshTokenKey = Keys.hmacShaKeyFor(decodedRefreshKey);
            
            jwtUtilLogger.info("JWT Secret Keys initialized successfully.");
        } catch (IllegalArgumentException e) {
            jwtUtilLogger.error("Error initializing JWT keys: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize JWT keys due to invalid Base64 secrets", e);
        }
    }

    public String generateAccessToken(String userEmail) {
        return generateToken(new HashMap<>(), userEmail, securityProperties.getAuth().getTokenExpiration().toMillis(), accessTokenKey);
    }

    public String generateRefreshToken(String userEmail) {
        return generateToken(new HashMap<>(), userEmail, securityProperties.getAuth().getRefreshTokenExpiration().toMillis(), refreshTokenKey);
    }

    private String generateToken(Map<String, Object> extraClaims, String subject, long expiration, SecretKey signingKey) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }

    public boolean validateToken(String token, UserDetails userDetails, boolean isRefreshToken) {
        final String username = extractUsername(token, isRefreshToken);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, isRefreshToken);
    }

    private boolean isTokenExpired(String token, boolean isRefreshToken) {
        return extractExpiration(token, isRefreshToken).before(new Date());
    }

    public Date extractExpiration(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getExpiration, isRefreshToken);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        final Claims claims = extractAllClaims(token, isRefreshToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, boolean isRefreshToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(isRefreshToken ? refreshTokenKey : accessTokenKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            jwtUtilLogger.error("Error parsing JWT token: {}", e.getMessage());
            throw new InvalidJWTTokenException("Invalid token format");
        }
    }

    public static Optional<String> preProcessingTokenChecks(Optional<String> authHeader) {
        return authHeader.filter(header -> header.startsWith(BEARER_PREFIX))
                .map(header -> header.substring(BEARER_PREFIX_LENGTH))
                .filter(token -> !token.isBlank());
    }

    public Claims getPayload(String token, boolean isRefreshToken) {
        return Optional.ofNullable(parseToken(token, isRefreshToken))
                .map(Jws::getBody)
                .orElseThrow(() -> new InvalidJWTTokenException("Invalid or malformed token"));
    }

    public Jws<Claims> parseToken(String token, boolean isRefreshToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(isRefreshToken ? refreshTokenKey : accessTokenKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            jwtUtilLogger.error("Error parsing JWT token: {}", e.getMessage());
            throw new InvalidJWTTokenException("Invalid token format");
        }
    }

    public long getRefreshTokenExpirationSeconds() {
        return securityProperties.getAuth().getRefreshTokenExpiration().toSeconds();
    }
}