package com.example.demo.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Add business logic to validate user else throw exception
		try {
		System.out.println("=============================Load========");
		System.out.println("=============================Load========"+User.builder().username(username).password(new BCryptPasswordEncoder().encode("abc")).passwordEncoder(p->p).build());
		}catch(Exception e) {
			System.out.println("=============================Before Exception========");

			e.printStackTrace();
			return null;
		}

		
		return User.builder().username(username).password(new BCryptPasswordEncoder().encode("abc")).passwordEncoder(p->p).build();
	}

}
