package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.authprovider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice.JWTUtil;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JWTUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationProvider(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = authentication.getCredentials().toString();

		// Extract username from JWT token
		String username = jwtUtil.extractUser(token).getEmail();

		if (username == null) {
			throw new BadCredentialsException("Invalid JWT token");
		}
		// Load user details from database
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtUtil.validateToken(token)) {
            throw new BadCredentialsException("Expired or invalid JWT token");
        }
	 return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
