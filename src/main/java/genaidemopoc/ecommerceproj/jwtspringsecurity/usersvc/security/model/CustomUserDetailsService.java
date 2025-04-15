package genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.security.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.AppConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.constants.LoggingConstants;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj.jwtspringsecurity.usersvc.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.debug("Attempting to load user by email: {}", email);
		
		try {
			Optional<UserEntity> userOpt = userRepository.findByEmail(email);
			
			if (userOpt.isEmpty()) {
				log.warn("User not found with email: {}", email);
				throw new UsernameNotFoundException(String.format(AppConstants.USER_NOT_FOUND_WITH_EMAIL, email));
			}
			
			UserEntity user = userOpt.get();
			log.debug("User found: {}. Roles: {}", email, user.getRoles());
			
			if (user.getRoles() == null || user.getRoles().isEmpty()) {
				log.warn("User found but has no roles: {}", email);
				// Assign default role to prevent authentication issues
				log.debug("Assigning default ROLE_USER for: {}", email);
				user.setRoles(List.of("ROLE_USER"));
				userRepository.save(user);
			}
			
			List<GrantedAuthority> authorities = user.getRoles().stream()
					.map(role -> {
						// Ensure role has ROLE_ prefix
						String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
						log.trace("Mapping role {} to authority: {}", role, formattedRole);
						return new SimpleGrantedAuthority(formattedRole);
					})
					.collect(Collectors.toList());
	
			log.debug("User {} has {} authorities: {}", 
					email, authorities.size(), 
					authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
			
			return new User(user.getEmail(), user.getPassword(), authorities);
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error loading user details for {}: {}", email, e.getMessage(), e);
			throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
		}
	}
}
