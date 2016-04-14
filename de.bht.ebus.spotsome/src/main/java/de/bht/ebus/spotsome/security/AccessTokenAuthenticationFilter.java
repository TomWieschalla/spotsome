package de.bht.ebus.spotsome.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {


	// ~ Instance fields
	// ==============================================================

	private AuthenticationProvider authenticationProvider;
	private AuthenticationEntryPoint authenticationEntryPoint;

	public AccessTokenAuthenticationFilter(AuthenticationProvider authenticationProvider,
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationProvider = Objects.requireNonNull(authenticationProvider);
		this.authenticationEntryPoint = Objects.requireNonNull(authenticationEntryPoint);
	}

	// ~ Methods
	// ==============================================================

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String token = request.getHeader("X-Access-Token");

		if (token == null) {
			logger.debug("\"X-Access-Token\" header field missing");
			filterChain.doFilter(request, response);
			return;
		}

		final AccessTokenAuthentication authRequest =
				new AccessTokenAuthentication(token);

		try {
			final Authentication authResult = authenticationProvider.authenticate(authRequest);
			SecurityContextHolder.getContext().setAuthentication(authResult);

			logger.debug("Authentication success");

		} catch (AuthenticationException failed) {
			logger.debug("Authentication request failed" + failed);

			authenticationEntryPoint.commence(request, response, failed);
			return;
		}

		filterChain.doFilter(request, response);
	}

}
