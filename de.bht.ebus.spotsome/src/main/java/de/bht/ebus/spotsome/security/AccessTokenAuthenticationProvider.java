package de.bht.ebus.spotsome.security;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.UserService;

public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

	// ~ Instance fields
	// ==============================================================
	
	private static final Logger logger = 
			LoggerFactory.getLogger(AccessTokenAuthenticationProvider.class);
	
	@Autowired
	private UserService userService;
	
	// ~ Methods
	// ==============================================================
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		final Object credentials = authentication.getCredentials();
		
		if (!(credentials instanceof String)) {
			logger.warn("Casting credentials to string failed. Credentials class is {}",
					credentials.getClass());
			throw new BadCredentialsException("Could not cast credentials to string");
		}
		
		final String token = (String) authentication.getCredentials();
		final User user = userService.getUserByAccessTokenStr(token);
		
		if (user == null) {
			logger.info("User does not exists");
			throw new BadCredentialsException("User does not exists");
		}
		
		final List<SimpleGrantedAuthority> grantedAuths = 
				Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		final Authentication auth = new AccessTokenAuthentication(user,
				token, grantedAuths);
		
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(AccessTokenAuthentication.class);
	}

}
