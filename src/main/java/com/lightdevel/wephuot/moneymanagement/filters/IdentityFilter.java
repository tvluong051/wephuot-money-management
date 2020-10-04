package com.lightdevel.wephuot.moneymanagement.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class IdentityFilter extends BasicAuthenticationFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private Map<String, Identity> identities = new HashMap<>();

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Identity {
		private String userId;
		private long expiresTokenTs;
	}

	private RestTemplate restTemplate;

	private String identityServerUrl;

	public IdentityFilter(AuthenticationManager authManager, RestTemplate restTemplate, String identityServerUrl) {
		super(authManager);
		this.restTemplate = Objects.requireNonNull(restTemplate);
		this.identityServerUrl = Objects.requireNonNull(identityServerUrl);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(AUTHORIZATION_HEADER);

		if (header == null || !header.startsWith(BEARER_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		long now = DateTime.now().getMillis();
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		log.debug("Token found: {}", authorizationHeader);
		if (authorizationHeader == null) {
			return null;
		}
		// parse the token.

		String token = StringUtils.replace(authorizationHeader, BEARER_PREFIX, "");
		Identity identity = null;
		if (identities.containsKey(token)) {
			identity = identities.get(token);
		}
		if (identity != null) {
			log.debug("now = {} and expireTs = {}", now, identity.getExpiresTokenTs());
			if (identity.getExpiresTokenTs() < now) {
				return  null;
			}
			log.debug("User {} found in cache for token", identity.getUserId());
			return new UsernamePasswordAuthenticationToken(identity.getUserId(), null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		}

		identity = validateToken(token);
		if (identity == null) {
			return null;
		}
		identities.put(token, identity);
		log.debug("User {} for token", identity.getUserId());
		return new UsernamePasswordAuthenticationToken(identity.getUserId(), null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	}

	private Identity validateToken(String token) {
		try {
			ResponseEntity<Identity> identity = restTemplate.postForEntity(String.format(this.identityServerUrl + "/api/v1/token/validation?token=%s", token), null, Identity.class);
			if(identity == null || identity.getBody() == null || identity.getBody().getUserId() == null) {
				log.error("not validate");
				return null;
			}
			return identity.getBody();
		} catch (Exception exception) {
			log.error("Cannot validate token {}", token);
		}
		return null;
	}
}
