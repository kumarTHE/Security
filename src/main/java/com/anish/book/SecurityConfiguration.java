package com.anish.book;

import java.net.http.HttpRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.springdoc.core.properties.SwaggerUiConfigProperties.Csrf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.security.PermitAll;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityfilterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(registry -> {
			registry.requestMatchers("/home","/register/**").permitAll();
			registry.requestMatchers("/user/**").hasRole("USER");
			registry.requestMatchers("/admin/**").hasRole("ADMIN");
			registry.anyRequest().authenticated();

		}).formLogin(formlogin->formlogin
				.loginPage("/login").successHandler(new AuthenticationSuccessHandler())
				.permitAll())
				.build();

	}
//	@Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//      return httpSecurity
//              .csrf(AbstractHttpConfigurer::disable)
//              .authorizeHttpRequests(registry -> {
//                  registry.requestMatchers("/home", "/register/**").permitAll();
//                  registry.requestMatchers("/admin/**").hasRole("ADMIN");
//                  registry.requestMatchers("/user/**").hasRole("USER");
//                  registry.anyRequest().authenticated();
//              })
//              .formLogin(httpSecurityFormLoginConfigurer -> {
//                  httpSecurityFormLoginConfigurer
//                          .loginPage("/login")
//                          .successHandler(new AuthenticationSuccessHandler())
//                          .permitAll();
//              })
//              .build();
	
	

	@Bean
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails normalUser = User.builder().username("anish")
//				.password("$2a$12$g31y5Cjk2mzxGwyaBI.lzeB8UMojj3tSQ4v/qDEzUZCn.YtHfXtbK")
//				.roles("USER")
//				.build();
//		UserDetails adminUser = User.builder().username("admin")
//				.password("$2a$12$g31y5Cjk2mzxGwyaBI.lzeB8UMojj3tSQ4v/qDEzUZCn.YtHfXtbK")
//				.roles("ADMIN", "USER")
//				.build();
//		return new InMemoryUserDetailsManager(normalUser, adminUser);
//	}
	
	@Autowired
CustomUserDetailsService userdetails;
	
	@Bean
	public UserDetailsService userdetailshaha() {
		return userdetails;
	}
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userdetails);
		provider.setPasswordEncoder(passwordencoder());
		return provider;
	}
}
