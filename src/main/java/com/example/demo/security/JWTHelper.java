package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.model.UserSubject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTHelper {
	
    public static final long JWT_TOKEN_VALIDITY = 2 * 3600;

    private String PRIVATE_KEY = "ryJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9ZyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQKflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5ckk";
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    
    private SecretKey getSignInKey() {
    	 byte[] bytes = PRIVATE_KEY.getBytes(StandardCharsets.UTF_8);
    	         return new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
    }
    
    //Generate token method
    public String generateToken(UserSubject userSubject) {
    	return Jwts.builder().setClaims(new HashMap<String, Object>())
    			.setSubject(userSubject.getUserEmail())
    			.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSignInKey(),signatureAlgorithm)
                .compact();
    	
    }
    
    // Get Subject
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Get Expiration Date
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Get Claim from JWT Token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Verify token and extract claims
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }
    
  
    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userDetail = getSubjectFromToken(token);
        return (userDetail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to verify token is expired or not
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

     
  
    

}
