package com.fashion.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new FashionUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());//gán đối tượng daoAuthenticationProvider
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {//cấu hình xác thực(authentication) và phân quyền(authorization)
		http.authorizeRequests()
					.antMatchers("/users/**").hasAuthority("Admin")
					.antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")

					.antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")

					.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
						.hasAnyAuthority("Admin", "Editor", "Salesperson")

					.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
						.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

					.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")

					.antMatchers("/products/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
				.anyRequest().authenticated()
				.and()
				.formLogin()			
					.loginPage("/login")
					.usernameParameter("email")
					.permitAll()
				.and().logout().permitAll()
				.and()
					.rememberMe()
						.key("AbcDefgHijKlmnOpqrs_1234567890")
						.tokenValiditySeconds(7 * 24 * 60 * 60);
		
		http.headers().frameOptions().sameOrigin();
	}		

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/ajax/**", "/css/**", "/webjars/**", "/Edge/**", "/fontawesome/**", "/icons/**", "/images/**", "/js/**", "/richtext/**", "/Trident/**", "/vendor/**", "/webfonts/**", "/error");
	}
}
