package com.jemhs.project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	SimpleAuthenticationSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// authorize requests
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/registration").permitAll()
				.antMatchers("/forgotPassword").permitAll()
				.antMatchers("/resetPassword").permitAll()
				.antMatchers("student/**").hasAnyAuthority("STUDENT,ADMIN")
				.antMatchers("/faculty/**").hasAnyAuthority("FACULTY,ADMIN")
				.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
				.authenticated().and().csrf().disable();
		
		//remember me configuration
				http.rememberMe(). 
				tokenRepository(persistentTokenRepository()).
		                rememberMeParameter("remember-me-param").
		                rememberMeCookieName("my-remember-me").
		                tokenValiditySeconds(86400);
				
		// login configuration
				http
				.formLogin().successHandler(successHandler)
				.loginPage("/login").failureUrl("/login?error=true")
				.usernameParameter("userName")
				.passwordParameter("password");
				
		// logout configuration		
				http
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and().exceptionHandling()
				.accessDeniedPage("/accessdenied");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		return tokenRepository;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}
