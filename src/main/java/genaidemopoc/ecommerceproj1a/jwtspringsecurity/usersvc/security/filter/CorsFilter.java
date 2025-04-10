package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this runs before other security filters
public class CorsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Add CORS headers to the response
		response.setHeader(SecurityConstants.ACCESS_CONTROL_ALLOW_ORIGIN, "*"); // Use constant
		/*
		 * Example: response.setHeader("Access-Control-Allow-Origin",
		 * "https://www.yourdomain.com");
		 */
		// response.setHeader(SecurityConstants.ACCESS_CONTROL_ALLOW_METHODS, SecurityConstants.ALLOWED_METHODS); // Handled by SecurityConfig
		// response.setHeader(SecurityConstants.ACCESS_CONTROL_ALLOW_HEADERS, SecurityConstants.ALLOWED_HEADERS); // Handled by SecurityConfig
		response.setHeader(SecurityConstants.ACCESS_CONTROL_EXPOSE_HEADERS, SecurityConstants.AUTHORIZATION); // Use constant

		// For preflight OPTIONS requests, return immediately with OK status
		if (SecurityConstants.OPTIONS.equalsIgnoreCase(request.getMethod())) { // Use constant
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		// Continue processing the request
		filterChain.doFilter(request, response);
	}
}
