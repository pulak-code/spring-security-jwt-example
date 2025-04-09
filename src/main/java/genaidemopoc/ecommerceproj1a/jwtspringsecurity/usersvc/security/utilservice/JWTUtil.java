package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.KeyGenerationException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private static final Logger jwtUtilLogger = LoggerFactory.getLogger(JWTUtil.class);
	private SecretKey signingKey;
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	@Value("${jwt.refresh-token.expiration}")
	private long refreshExpiration;
	private final UserService userService;

	// Constructor: loads existing secret or generates new one if missing.
	public JWTUtil(@Value("${jwt.secret.key:}") String secretKeyProperty, @Lazy UserService userService) {
		signingKey = getNewSigningKey(secretKeyProperty);
		this.userService = userService;
	}

	// Inner class to hold token validation results
	private String createJwtToken(Map<String, Object> claims, long expiryTime) {
		return Jwts.builder().claims(claims).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiryTime)).signWith(getSigningKey()).compact();
	}

	// Generate Access Token (Includes user claims)
	public String generateAccessToken(String userEmail) {
		return generateToken(new HashMap<>(), userEmail, jwtExpiration);
	}

	// Generate Refresh Token (Minimal claims)
	public String generateRefreshToken() {
		return generateToken(new HashMap<>(), UUID.randomUUID().toString(), refreshExpiration);
	}

	private String generateToken(Map<String, Object> extraClaims, String subject, long expiration) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser()
					.verifyWith(getSigningKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (Exception e) {
			jwtUtilLogger.error("Error parsing JWT token: {}", e.getMessage());
			throw new InvalidJWTTokenException("Invalid token format");
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

	/**
	 * Get the refresh token expiration time in seconds
	 * @return refresh token expiration time in seconds
	 */
	public long getRefreshExpiration() {
		return refreshExpiration / 1000; // Convert milliseconds to seconds
	}
}
