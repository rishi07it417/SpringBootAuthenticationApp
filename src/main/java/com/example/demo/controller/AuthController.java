package com.example.demo.controller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.JWTRequest;
import com.example.demo.model.JWTResponse;
import com.example.demo.model.UserSubject;
import com.example.demo.model.ValidateResponse;
import com.example.demo.security.JWTHelper;
import com.example.demo.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager manager;


    @Autowired
    private JWTHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
   	 	 String token = "Bad Credentials";

    	 try {
    		 manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
    		 
          	token = this.helper.generateToken(new UserSubject(request.getEmail()));
          	System.out.println("=====token==============="+token);
    		 
    		 JWTResponse response = new JWTResponse();
             response.setJwtToken(token);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
    	 }catch(Exception e) {
    		 e.printStackTrace();
    		 JWTResponse response = new JWTResponse();
             response.setJwtToken(token);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
    	 }


	     
	     

	     

        
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ValidateResponse> Verify(@RequestBody JWTResponse response) {
        System.out.println("============================================ValidateResponse start======================");

    	ValidateResponse res = new ValidateResponse();
    	if(SecurityContextHolder.getContext().getAuthentication()!=null) {
    		res.setMessage("Verified");
    	}else {
            System.out.println("============================================not verify start======================");

    		res.setMessage("Invalid Token");

    	}
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

   

    
}
