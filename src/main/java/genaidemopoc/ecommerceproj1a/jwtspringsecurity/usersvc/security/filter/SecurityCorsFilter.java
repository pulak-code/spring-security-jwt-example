package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.config.SecurityProperties;

/**
 * Custom CORS filter that ensures CORS headers are added before any security processing.
 * This filter runs before Spring Security filters to ensure CORS is handled properly.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SecurityCorsFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityCorsFilter.class);
    
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            
        String origin = request.getHeader("Origin");
        
        SecurityProperties.Cors corsConfig = securityProperties.getCors();
        
        if (origin != null && corsConfig.getAllowedOrigins().contains(origin)) {
            try {
                // Set CORS headers
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Methods", 
                    String.join(",", corsConfig.getAllowedMethods()));
                response.setHeader("Access-Control-Allow-Headers", 
                    String.join(",", corsConfig.getAllowedHeaders()));
                response.setHeader("Access-Control-Expose-Headers", 
                    String.join(",", corsConfig.getExposedHeaders()));
                response.setHeader("Access-Control-Allow-Credentials", 
                    corsConfig.getAllowCredentials().toString());
                response.setHeader("Access-Control-Max-Age", 
                    corsConfig.getMaxAge().toString());

                // Handle preflight requests
                if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
                
                logger.debug("CORS headers set for origin: {}", origin);
            } catch (Exception e) {
                logger.error("Error setting CORS headers: {}", e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }
}

