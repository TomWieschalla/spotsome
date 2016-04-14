package de.bht.ebus.spotsome.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AccessTokenAuthentication extends UsernamePasswordAuthenticationToken {
	
	private static final long serialVersionUID = 1L;

	public AccessTokenAuthentication(Object credentials) {
		super(null, credentials);
	}
	
	public AccessTokenAuthentication(Object principal, Object credentials) {
		super(principal, credentials);
	}
	
	public AccessTokenAuthentication(Object principal,
			Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}
	
}
