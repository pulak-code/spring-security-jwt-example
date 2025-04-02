package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.utilservice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.service.TokenBlacklistService;

/**
 * Service for managing blacklisted JWT tokens.
 * 
 * TODO: For production and distributed environments, replace the in-memory map with:
 * 1. Redis - For fast token validation and automatic expiry capabilities
 * 2. MongoDB - For persistent storage with TTL indexes for automatic cleanup
 * 
 * This would solve issues with:
 * - Server restarts (current implementation loses blacklist on restart)
 * - Distributed deployments (multiple service instances need shared blacklist)
 * - Scalability (in-memory solution won't scale for high traffic)
 */
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
	private final Map<String, Long> blackList = new ConcurrentHashMap<>();
	private JWTUtil jwtUtil;
	private static final Logger tokenBlackListLogger = LoggerFactory.getLogger(TokenBlacklistServiceImpl.class);

	public TokenBlacklistServiceImpl(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public boolean isTokenBlacklisted(String token) {
		tokenBlackListLogger.info(LoggingUserServiceConstants.LOG_TOKEN_VALIDATION, token);
		boolean isBlacklisted = blackList.containsKey(token);
		if (isBlacklisted) {
			tokenBlackListLogger.info(LoggingUserServiceConstants.LOG_TOKEN_INVALID, token);
		} else {
			tokenBlackListLogger.info(LoggingUserServiceConstants.LOG_TOKEN_VALID, token);
		}
		return isBlacklisted;
	}

	@Override
	public void blacklistToken(String token, Long expiryTime) {
		blackList.put(token, expiryTime);
		tokenBlackListLogger.info(LoggingUserServiceConstants.LOG_TOKEN_BLACKLISTED,
				jwtUtil.parseToken(token).getPayload().getSubject());
		tokenBlackListLogger.debug("Token added to blacklist");
	}

	@Override
	public void removeFromBlacklist(String token) {
		blackList.remove(token);
		tokenBlackListLogger.info(LoggingUserServiceConstants.LOG_TOKEN_REMOVED, token);
	}

	// Cleanup expired tokens (removes them from memory)
	@Scheduled(fixedRate = 3000001)
	public void removeExpiredTokens() {
		long countBefore = blackList.size();
		blackList.entrySet().removeIf(e -> e.getValue() < System.currentTimeMillis());
		long removed = countBefore - blackList.size();
		if (removed > 0) {
			tokenBlackListLogger.info("Removed {} expired tokens from blacklist", removed);
		}
	}
}
