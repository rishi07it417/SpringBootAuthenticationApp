package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.security.JWTAuthenticationEntryPoint;
import com.example.demo.security.JWTAuthenticationFilter;
import com.example.demo.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class AppConfig {
	
	 	@Autowired
	    private JWTAuthenticationEntryPoint exceptionHandlingObj;
	    
	 	@Autowired
	    private JWTAuthenticationFilter securityFilter;
	 	
	 	@Autowired
	 	private UserDetailsServiceImpl userDetailService;

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http.csrf(csrf -> csrf.disable())
	        	.cors(cors -> cors.disable())
	                	.authorizeHttpRequests(auth ->  {
								auth.requestMatchers("/auth/verify").authenticated()
															   .requestMatchers("/auth/login").permitAll()
															   .anyRequest().authenticated();
							
						}
	                  )
					 .exceptionHandling(ex -> ex.authenticationEntryPoint(exceptionHandlingObj))
	                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
	        
	        
	        return http.build();
	    }
	    
	    @Bean
	    public PasswordEncoder getPasswordEncoder() {
	    	return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder ) {
	    	try {
		    	return builder.getAuthenticationManager();

	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	return null;
	    }

	    
	    
	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder authBuilder) {
	    	try {
				authBuilder.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	  
	    
	   
}
