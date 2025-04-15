package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.SecurityConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.InvalidJWTTokenException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTTokenVerificationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTTokenVerificationFilter.class);
	private static final String AUTHORIZATION_HEADER = SecurityConstants.AUTHORIZATION;
	private static final String BEARER_PREFIX = SecurityConstants.TOKEN_PREFIX;
	
	// Paths that should bypass JWT verification
	private static final List<String> SKIP_URLS = Arrays.asList(
		SecurityConstants.LOGIN_URL,
		SecurityConstants.SIGN_UP_URL,
		SecurityConstants.REFRESH_TOKEN_ENDPOINT,
		AppConstants.AUTH_BASE_PATH + "/test/"
	);

	private final UserDetailsService userDetailsService;
	private final JWTUtil jwtUtil;

	@Autowired
	public JWTTokenVerificationFilter(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Start with detailed logging for authentication debugging
		String requestURI = request.getRequestURI();
		logger.debug("Processing request: {} {}", request.getMethod(), requestURI);
		
		if (shouldSkipFilter(requestURI)) {
			logger.debug("Skipping JWT filter for whitelisted path: {}", requestURI);
			filterChain.doFilter(request, response);
			return;
		}

		// Extract token from Request
		final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		logger.debug("Auth header: {}", authHeader);
		
		String jwt = null;
		String userEmail = null;

		Optional<String> token = jwtUtil.preProcessingTokenChecks(Optional.ofNullable(authHeader));
		if (token.isPresent()) {
			jwt = token.get();
			try {
				userEmail = jwtUtil.extractUsername(jwt, false);
				logger.debug("Extracted username from token: {}", userEmail);
			} catch (InvalidJWTTokenException e) {
				logger.warn("Invalid JWT Token: {}", e.getMessage());
			}
		} else {
			logger.debug("No JWT token found in request headers");
		}

		// Validate token and set authentication if valid
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.debug("Processing token for user: {}", userEmail);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
			
			try {
				boolean isValid = jwtUtil.validateToken(jwt, userDetails, false);
				logger.debug("Token validation result: {}", isValid);
				
				if (isValid) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					logger.debug("Setting authentication for user: {}", userEmail);
					logger.debug("Authorities: {}", userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
					
					// Record successful authentication
					logger.info("Authentication successful for user: {}", userEmail);
				} else {
					logger.warn("Token validation failed for user: {}", userEmail);
				}
			} catch (Exception e) {
				logger.error("Error validating token: {}", e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}
	
	/**
	 * Determines whether the JWT authentication filter should be skipped for a given URI.
	 * 
	 * @param uri the request URI
	 * @return true if the filter should skip processing this request
	 */
	private boolean shouldSkipFilter(String uri) {
		return SKIP_URLS.stream()
				.anyMatch(uri::startsWith);
	}
} 