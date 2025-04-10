package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.authprovider;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.FailedLoginAttempt;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.FailedLoginAttemptRepository;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.metrics.AuthenticationMetricsService;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

/**
 * Custom authentication provider that handles user authentication
 * with additional security checks and logging.
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    
    private final CustomUserDetailsService userDetailsService;
    private final @Lazy PasswordEncoder passwordEncoder;
    private final AuthenticationMetricsService metricsService;
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        logger.debug("Processing authentication request for user: {}", email);

        try {
            // Check for account lockout first
            checkAccountLockout(email);

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Verify password
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                logger.warn("Authentication failed: Invalid credentials for user: {}", email);
                handleFailedLogin(email);
                metricsService.recordAuthenticationFailure();
                throw new InvalidCredentialsException("Invalid email or password");
            }
            
            // Verify user is enabled and not locked
            if (!userDetails.isEnabled()) {
                metricsService.recordAuthenticationFailure();
                throw new DisabledException("Account is disabled");
            }

            if (!userDetails.isAccountNonLocked()) {
                metricsService.recordAuthenticationFailure();
                throw new LockedException("Account is locked");
            }

            // Verify user has required roles
            boolean hasValidRole = userDetails.getAuthorities().stream()
                .anyMatch(authority -> 
                    authority.getAuthority().equals("ROLE_USER") || 
                    authority.getAuthority().equals("ROLE_ADMIN")
                );

            if (!hasValidRole) {
                logger.warn("Authentication failed: User {} does not have required roles", email);
                metricsService.recordAuthenticationFailure();
                throw new BadCredentialsException("User does not have required roles");
            }

            // Create authenticated token with user details and authorities
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // credentials are cleared for security
                    userDetails.getAuthorities()
                );

            logger.info("Successfully authenticated user: {}", email);
            resetFailedAttempts(email);
            metricsService.recordAuthenticationSuccess();

            return authToken;

        } catch (AuthenticationException e) {
            metricsService.recordAuthenticationFailure();
            logger.error("Authentication failed for user {}: {}", email, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Authentication error occurred for user {}: {}", email, e.getMessage());
            metricsService.recordAuthenticationFailure();
            throw new BadCredentialsException("Authentication failed", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void handleFailedLogin(String email) {
        FailedLoginAttempt attempt = failedLoginAttemptRepository.findByEmail(email)
            .orElse(FailedLoginAttempt.builder()
                .email(email)
                .attempts(0)
                .build());

        attempt.setAttempts(attempt.getAttempts() + 1);
        attempt.setLastFailedAttempt(LocalDateTime.now());

        if (attempt.getAttempts() >= MAX_FAILED_ATTEMPTS) {
            attempt.setLocked(true);
            attempt.setLockoutEndTime(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
            logger.warn("Account locked for user {} due to {} failed attempts", email, MAX_FAILED_ATTEMPTS);
        }

        failedLoginAttemptRepository.save(attempt);
    }

    private void resetFailedAttempts(String email) {
        failedLoginAttemptRepository.findByEmail(email).ifPresent(attempt -> {
            attempt.setAttempts(0);
            attempt.setLocked(false);
            attempt.setLockoutEndTime(null);
            failedLoginAttemptRepository.save(attempt);
            logger.info("Reset failed login attempts for user: {}", email);
        });
    }

    private void checkAccountLockout(String email) {
        failedLoginAttemptRepository.findByEmail(email).ifPresent(attempt -> {
            if (attempt.isLocked()) {
                if (attempt.getLockoutEndTime().isAfter(LocalDateTime.now())) {
                    logger.warn("Login attempted on locked account: {}", email);
                    throw new LockedException("Account is temporarily locked. Please try again later.");
                } else {
                    // Lockout period has expired
                    resetFailedAttempts(email);
                }
            }
        });
    }
}

