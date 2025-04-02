package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.KeyGenerationException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.TokenBlacklistService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private static final Logger jwtUtilLogger = LoggerFactory.getLogger(JWTUtil.class);
	private SecretKey signingKey;
	@Value("${security.jwt.refresh-expiration-ms}")
	private long refreshTokenExpirationTime;
	@Value("${security.jwt.expiration-ms:2400000}") // Default: 40 minutes if not set private
	long accessTokenExpirationTime;
	private final UserService userService;

	// Constructor: loads existing secret or generates new one if missing.
	public JWTUtil(@Value("${jwt.secret.key:}") String secretKeyProperty,@Lazy UserService userService) {
		signingKey= getNewSigningKey(secretKeyProperty);
		this.userService = userService;
	}

	// Inner class to hold token validation results
	private String createJwtToken(Map<String, Object> claims, long expiryTime) {
		return Jwts.builder().claims(claims).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiryTime)).signWith(getSigningKey()).compact();
	}

	// Generate Access Token (Includes user claims)
	public String generateAccessToken(UserEntity user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", user.getId());
		claims.put("roles", getFormattedRoles(user));
		claims.put("sub", user.getEmail());
		return createJwtToken(claims, accessTokenExpirationTime);
	}

	// Generate Refresh Token (Minimal claims)
	public String generateRefreshToken(String userEmail) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userEmail); // Subject (user email)
		claims.put("jti", UUID.randomUUID().toString()); // Unique token ID
		claims.put("iat", System.currentTimeMillis()); // Issued at time
		claims.put("type", "refresh");
		return createJwtToken(claims, refreshTokenExpirationTime);
	}

	// Rotate Refresh Token (Blacklist old one)
	public String generateRotatedRefreshToken(String oldToken, TokenBlacklistService tokenBlacklistService) {
		String newToken = generateRefreshToken(extractUser(oldToken).getEmail());
		tokenBlacklistService.blacklistToken(oldToken, getExpiration(oldToken).getTime());
		return newToken;
	}

	public String extractUsername(Jws<Claims> claims) {
		Claims payload = claims.getPayload();
		String subject = null;
		if (null != payload)
			subject = payload.getSubject();
		return subject;
	}

	public long getExpiryTime(Jws<Claims> parsedToken) {
		return parsedToken.getPayload().getExpiration().getTime();
	}

	// Extract Expiry Time
	public Date getExpiration(String token) {
		return getPayload(token).getExpiration();
	}

	// Validate & Parse JWT
	public boolean validateToken(String token) {
		if (token == null || token.isBlank()) {
	        jwtUtilLogger.warn("Empty or null token received.");
	        return false;
	    }
		try {
			Jws<Claims> claims = parseToken(token); // Step 1: Parse token (signature check)
			// Step 2: Expiry validation
			if (claims.getPayload().getExpiration().before(new Date())) {
				jwtUtilLogger.warn("Token is expired.");
				return false;
			}
			// Step 3: User validation
			String userEmail = claims.getPayload().getSubject();
			if (!userService.userExists(userEmail)) { // Use UserService
				jwtUtilLogger.warn("Token validation failed: User does not exist.");
				return false;
			}
			return true; // Token is valid
		} catch (JwtException | IllegalArgumentException e) {
			jwtUtilLogger.warn("Token validation failed: {}", e.getMessage());
			return false;
		}
	}

	// Format Roles Properly
	private List<String> getFormattedRoles(UserEntity user) {
		return Optional.ofNullable(user.getRoles()).orElse(Collections.emptyList()).stream()
				.map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase()).toList();
	}

	// Generate a new secret key (Base64-encoded) if not provided
	private String generateSecretKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(UserServiceConstants.KEYGEN_HASHALGO);
			keyGenerator.init(256); // 256-bit key
			byte[] encodedSecretKey = keyGenerator.generateKey().getEncoded();
			return Base64.getEncoder().encodeToString(encodedSecretKey);
		} catch (Exception e) {
			throw new KeyGenerationException(UserServiceConstants.ERROR_GENERATING_KEY, e);
		}
	}

	// Convert the Base64 secret string into a SecretKey
	private SecretKey getNewSigningKey(String secretKeyProperty) {
		byte[] decodeKey = Base64.getDecoder().decode(secretKeyProperty);
		return Keys.hmacShaKeyFor(decodeKey);
	}

	public static Optional<String> preProcessingTokenChecks(Optional<String> authHeader) {
		return authHeader.filter(header -> header.startsWith("Bearer ")) // Check if it starts with "Bearer "
				.map(header -> header.substring(7)) // Extract the token after "Bearer "
				.filter(token -> !token.isBlank());
	}

	public UserEntity extractUser(String token) {
		Claims payload = getPayload(token);
		String subject = null;
		if (null != payload)
			subject = payload.getSubject();
		return userService.getUserByEmail(subject);
	}

	public Claims getPayload(String token) {
		return Optional.ofNullable(parseToken(token)).map(Jws::getPayload)
				.orElseThrow(() -> new InvalidJWTTokenException("Invalid or malformed token"));
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token); // Parses and verifies
																							// token signature
	}

	private SecretKey getSigningKey() {
		return signingKey;
	}
}
