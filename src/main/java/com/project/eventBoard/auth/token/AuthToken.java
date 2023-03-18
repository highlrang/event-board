package com.project.eventBoard.auth.token;

import java.security.Key;
import java.util.Date;

import com.project.eventBoard.user.domain.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthToken(String id, Role role, Date expiryDate, Key key){
        this.key = key;
        this.token = createAuthToken(id, role.name(), expiryDate);
    }
    
    private String createAuthToken(String id, String role, Date expiryDate) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiryDate)
                .compact();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");

        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");

        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");

        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        
        return null;
    }
}
