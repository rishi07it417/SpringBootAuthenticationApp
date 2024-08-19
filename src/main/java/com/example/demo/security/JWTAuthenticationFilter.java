package com.example.demo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.ValidateResponse;
import com.example.demo.services.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
   
    @Autowired
    private JWTHelper jWTHelper;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		  String requestHeader = request.getHeader("Authorization");
		  try {
	        System.out.println("============================================doFilterInternal starts======================");

		  logger.info(" Header : ", requestHeader);
	        String subject = null;
	        String token = null;
	        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
	            //looking good
	            token = requestHeader.substring(7);
	            try {

	                subject = this.jWTHelper.getSubjectFromToken(token);

	            } catch (IllegalArgumentException e) {
	                logger.info("Illegal Argument while fetching the subject ");
	                e.printStackTrace();
	            } catch (ExpiredJwtException e) {
	                logger.info("JWT token is expired");
	                e.printStackTrace();
	            } catch (MalformedJwtException e) {
	                logger.info("Invalid Token");
	                e.printStackTrace();
	            } catch (Exception e) {
	                e.printStackTrace();

	            }


	        } else {
	            logger.info("Invalid Header Value");
	        }


	        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {


	            UserDetails userDetails =  this.userDetailsService.loadUserByUsername(subject);
	            Boolean validateToken = this.jWTHelper.validateToken(token, userDetails);
	            if (validateToken) {

	                //set the authentication
	            	
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, userDetails.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);


	            } else {
	                logger.info("Validation fails");
	            }


	        }else {
	        
	                System.out.println("============================================not verify start======================");

	                System.out.println("============================================not verify start======================"+response.getStatus());

	        	
	        }
	        

	        
	        System.out.println("============================================doFilterInternal end======================");
		  }catch(Exception e){
			  e.printStackTrace();
		}
		  
          System.out.println("============================================Before chain======================");

	        filterChain.doFilter(request, response);
		
	}
}
