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

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.KeyGenerationException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import java.nio.charset.StandardCharsets;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;

@Component
public class JWTUtil {
	private static final Logger jwtUtilLogger = LoggerFactory.getLogger(JWTUtil.class);
	@Value("${jwt.expiration}")
	private long jwtExpirationMs;
	@Value("${jwt.refresh-token.expiration}")
	private long refreshTokenExpirationMs;
	private final UserService userService;

	@Value("${jwt.secret}")
	private String jwtSecret;
	private SecretKey key;

	public JWTUtil(@Lazy UserService userService) {
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		try {
			byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
			this.key = Keys.hmacShaKeyFor(decodedKey);
			jwtUtilLogger.info("JWT Secret Key initialized successfully.");
		} catch (IllegalArgumentException e) {
			jwtUtilLogger.error("Error initializing JWT key: {}", e.getMessage());
			throw new RuntimeException("Failed to initialize JWT key due to invalid Base64 secret", e);
		}
	}

	public String generateAccessToken(String userEmail) {
		return generateToken(new HashMap<>(), userEmail, jwtExpirationMs);
	}

	public String generateRefreshToken(String userEmail) {
		return generateToken(new HashMap<>(), userEmail, refreshTokenExpirationMs);
	}

	private String generateToken(Map<String, Object> extraClaims, String subject, long expiration) {
		return Jwts.builder()
				.setClaims(extraClaims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(key, SignatureAlgorithm.HS256)
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
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			jwtUtilLogger.error("Error parsing JWT token: {}", e.getMessage());
			throw new InvalidJWTTokenException("Invalid token format");
		}
	}

	public static Optional<String> preProcessingTokenChecks(Optional<String> authHeader) {
		return authHeader.filter(header -> header.startsWith("Bearer "))
				.map(header -> header.substring(7))
				.filter(token -> !token.isBlank());
	}

	public Claims getPayload(String token) {
		return Optional.ofNullable(parseToken(token)).map(Jws::getBody)
				.orElseThrow(() -> new InvalidJWTTokenException("Invalid or malformed token"));
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public long getRefreshTokenExpirationSeconds() {
		return refreshTokenExpirationMs / 1000;
	}
}
