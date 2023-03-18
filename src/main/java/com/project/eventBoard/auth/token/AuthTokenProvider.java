package com.project.eventBoard.auth.token;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.groovy.syntax.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.project.eventBoard.auth.dto.OAuth2UserInfo;
import com.project.eventBoard.user.domain.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

public class AuthTokenProvider{

    @Value("${token.expiry}")
    private String tokenExpiry;

    private static final String AUTHORITIES_KEY = "role";

    private Key key;
    public AuthTokenProvider(String secretKey){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    public AuthToken createAuthToken(String id, Role role) {
        Date expiryDate = getExpiryDate();
        return new AuthToken(id, role, expiryDate, key);
    }

    public AuthToken convertAuthToken(String token){
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) throws TokenException{
        
        if(!authToken.validate())
            throw new TokenException("TOKEN_VALIDATION_FAIL", null);

        Claims tokenClaims = authToken.getTokenClaims();
        Collection<? extends GrantedAuthority> authorities = 
            Arrays.stream(new String[]{tokenClaims.get(AUTHORITIES_KEY).toString()})
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = new User(tokenClaims.getSubject(), null, authorities); 
        return new UsernamePasswordAuthenticationToken(user, authToken, authorities);
    
    }

    private Date getExpiryDate(){
        return new Date(new Date().getTime() + Long.parseLong(tokenExpiry));
    }
    
}
