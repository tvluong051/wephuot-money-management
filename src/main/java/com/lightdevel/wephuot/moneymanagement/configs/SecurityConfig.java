package com.lightdevel.wephuot.moneymanagement.configs;

import com.lightdevel.wephuot.moneymanagement.filters.IdentityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

	private final RestTemplate restTemplate;
	private final String identityServerUrl;

	@Autowired
	public SecurityConfig(@Value("${services.social-circle.url}") String identityServerUrl, RestTemplate restTemplate) {
		this.restTemplate = Objects.requireNonNull(restTemplate);
		this.identityServerUrl = Objects.requireNonNull(identityServerUrl);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/api/**").authenticated()
			.and()
				.addFilter(new IdentityFilter(authenticationManager(), restTemplate, identityServerUrl))
		;
	}
}
