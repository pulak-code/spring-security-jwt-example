package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.FailedLoginAttempt;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.FailedLoginAttemptRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for cleaning up expired account lockouts.
 * Runs on a scheduled basis to maintain database hygiene and
 * ensure accounts are properly unlocked after their lockout period.
 */
@Service
@RequiredArgsConstructor
public class LockoutCleanupService {
    private static final Logger logger = LoggerFactory.getLogger(LockoutCleanupService.class);
    
    private final FailedLoginAttemptRepository failedLoginAttemptRepository;
    private final MeterRegistry meterRegistry;
    
    private Counter cleanupCounter;
    private Counter failedCleanupCounter;
    
    public void initMetrics() {
        cleanupCounter = Counter.builder("security.lockout.cleanup")
            .description("Number of expired lockouts cleaned up")
            .tag("type", "success")
            .register(meterRegistry);
            
        failedCleanupCounter = Counter.builder("security.lockout.cleanup")
            .description("Number of failed cleanup attempts")
            .tag("type", "failure")
            .register(meterRegistry);
    }
    
    /**
     * Scheduled task to clean up expired lockouts.
     * Runs every 5 minutes by default.
     */
    @Scheduled(fixedDelayString = "${security.lockout.cleanup.interval:300000}")
    @Transactional
    public void cleanupExpiredLockouts() {
        logger.debug("Starting scheduled cleanup of expired lockouts");
        
        try {
            List<FailedLoginAttempt> expiredLockouts = findExpiredLockouts();
            
            if (!expiredLockouts.isEmpty()) {
                processExpiredLockouts(expiredLockouts);
                cleanupCounter.increment(expiredLockouts.size());
                logger.info("Successfully cleaned up {} expired lockouts", expiredLockouts.size());
            } else {
                logger.debug("No expired lockouts found during cleanup");
            }
            
        } catch (Exception e) {
            failedCleanupCounter.increment();
            logger.error("Error during lockout cleanup: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Finds all lockouts that have expired.
     */
    @Transactional(readOnly = true)
    public List<FailedLoginAttempt> findExpiredLockouts() {
        return failedLoginAttemptRepository.findExpiredLockouts(LocalDateTime.now());
    }
    
    /**
     * Process expired lockouts by resetting attempt counts and updating timestamps.
     */
    @Transactional
    public void processExpiredLockouts(List<FailedLoginAttempt> expiredLockouts) {
        LocalDateTime now = LocalDateTime.now();
        
        for (FailedLoginAttempt lockout : expiredLockouts) {
            try {
                failedLoginAttemptRepository.resetAttempts(lockout.getEmail(), now);
                logger.debug("Reset lockout for user: {}", lockout.getEmail());
            } catch (Exception e) {
                failedCleanupCounter.increment();
                logger.error("Failed to reset lockout for user {}: {}", 
                    lockout.getEmail(), e.getMessage(), e);
            }
        }
    }
    
    /**
     * Manually trigger a cleanup of expired lockouts.
     * Useful for testing or administrative purposes.
     */
    @Transactional
    public void manualCleanup() {
        logger.info("Manual cleanup of expired lockouts triggered");
        cleanupExpiredLockouts();
    }
}

