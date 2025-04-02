package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.SecurityConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger authFilterLogger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final JWTUtil jwtUtil;
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	private final TokenBlacklistService tokenBlacklistService;

	public JWTAuthenticationFilter(JWTUtil jwtUtil,
			  TokenBlacklistService tokenBlacklistService) {
		this.jwtUtil = jwtUtil;
		this.tokenBlacklistService = tokenBlacklistService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String authHeader = request.getHeader(SecurityUserServiceConstants.JWT_HEADER);
			if (authHeader != null && authHeader.startsWith(SecurityUserServiceConstants.JWT_PREFIX)) {
				String token = authHeader.substring(SecurityUserServiceConstants.JWT_PREFIX.length());
				authFilterLogger.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);

				if (tokenBlacklistService.isTokenBlacklisted(token)) {
					authFilterLogger.error(LoggingUserServiceConstants.LOG_TOKEN_INVALID, token);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				if (jwtUtil.validateToken(token)) {
					String username = jwtUtil.extractUser(token).getEmail();
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					authFilterLogger.info(LoggingUserServiceConstants.LOG_AUTH_SUCCESS, username);
				} else {
					authFilterLogger.error(LoggingUserServiceConstants.LOG_TOKEN_INVALID, token);
				}
			}
		} catch (Exception e) {
			authFilterLogger.error(LoggingUserServiceConstants.LOG_EXCEPTION, e.getMessage());
		}
		filterChain.doFilter(request, response);
	}

}
