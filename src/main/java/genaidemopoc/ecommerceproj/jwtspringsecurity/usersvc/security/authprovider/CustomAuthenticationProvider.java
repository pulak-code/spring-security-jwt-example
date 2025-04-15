package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.authprovider;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.InvalidCredentialsException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.FailedLoginAttempt;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.FailedLoginAttemptRepository;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.metrics.AuthenticationMetricsService;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.model.CustomUserDetailsService;
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

        logger.info("Processing authentication request for user: {}", email);

        try {
            // Load user details
            logger.info("Loading user details for: {}", email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            
            // Log details for debugging
            logger.info("User found: {}, Authorities: {}", email, userDetails.getAuthorities());
            
            // Verify password
            logger.info("Verifying password for user: {}", email);
            boolean passwordMatches = passwordEncoder.matches(password, userDetails.getPassword());
            logger.info("Password match result: {}", passwordMatches);
            
            if (!passwordMatches) {
                logger.warn("Password verification failed for user: {}", email);
                metricsService.recordAuthenticationFailure();
                throw new BadCredentialsException("Invalid email or password");
            }
            
            // Create authenticated token with user details and authorities
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // credentials are cleared for security
                    userDetails.getAuthorities()
                );
            
            logger.info("Authentication successful for user: {}", email);
            metricsService.recordAuthenticationSuccess();
            
            return authToken;
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found: {}", email);
            metricsService.recordAuthenticationFailure();
            throw new BadCredentialsException("Invalid email or password");
        } catch (BadCredentialsException e) {
            logger.warn("Bad credentials: {}", e.getMessage());
            metricsService.recordAuthenticationFailure();
            throw e;
        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage(), e);
            metricsService.recordAuthenticationFailure();
            throw new BadCredentialsException("Authentication failed: " + e.getMessage());
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

