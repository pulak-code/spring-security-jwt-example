package genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.security.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.LoggingUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.constant.MessageUserServiceConstants;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.exception.custom.UserNotFoundException;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.model.UserEntity;
import genaidemopoc.ecommerceproj1a.jwtspringsecurity.usersvc.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info(LoggingUserServiceConstants.LOG_USER_EXISTS, email);
		
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> {
					log.error(MessageUserServiceConstants.USER_NOT_FOUND);
					return new UserNotFoundException(MessageUserServiceConstants.USER_NOT_FOUND);
				});
		
		log.info(LoggingUserServiceConstants.LOG_USER_EXISTS, email);
		List<GrantedAuthority> authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
}
