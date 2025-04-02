package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.UserServiceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Add CORS headers to the response
		response.setHeader(UserServiceConstants.ACCESS_CONTROL_ALLOW_ORIGIN, "*"); // For production, restrict this to specific origins
		/*
		 * Example: response.setHeader("Access-Control-Allow-Origin",
		 * "https://www.yourdomain.com");
		 */
		response.setHeader(UserServiceConstants.ACCESS_CONTROL_ALLOW_METHODS, UserServiceConstants.HTTP_METHODS);
		response.setHeader(UserServiceConstants.ACCESS_CONTROL_ALLOW_HEADERS, UserServiceConstants.HTTP_HEADERS);
		response.setHeader(UserServiceConstants.ACCESS_CONTROL_EXPOSE_HEADERS, UserServiceConstants.AUTHORIZATION);

		// For preflight OPTIONS requests, return immediately with OK status
		if (UserServiceConstants.OPTIONS.equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		// Continue processing the request
		filterChain.doFilter(request, response);
	}
}
