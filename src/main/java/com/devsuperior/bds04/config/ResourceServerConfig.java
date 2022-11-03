package com.devsuperior.bds04.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private JwtTokenStore tokenStore;

	private static final String ADMIN_ROLE = "ADMIN";
	private static final String CLIENT_ROLE = "CLIENT";
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	private static final String CITIES_ENDPOINT = "/cities/**";
	private static final String EVENTS_ENDPOINT = "/events/**";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	// @formatter:off
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// enable h2 web console for test env
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET, CITIES_ENDPOINT, EVENTS_ENDPOINT).permitAll()
		.antMatchers(HttpMethod.POST, EVENTS_ENDPOINT).hasAnyRole(ADMIN_ROLE, CLIENT_ROLE)
		.anyRequest().hasAnyRole(ADMIN_ROLE);
	}

}
