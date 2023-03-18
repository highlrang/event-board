package com.project.eventBoard.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.groovy.syntax.TokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.eventBoard.auth.token.AuthToken;
import com.project.eventBoard.auth.token.AuthTokenProvider;
import com.project.eventBoard.util.HeaderUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenStr = HeaderUtil.getToken(request);

        if (tokenStr != null){

            AuthToken token = null; // tokenProvider.convertAuthToken(tokenStr);

            if(token.validate()) {
                try{
                    Authentication authentication = null; // tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }catch(Exception e){
                    log.error("[토큰 필터 오류] error = {}, msg = {} \n{}", e.toString(), e.getMessage(), Arrays.toString(e.getStackTrace()));
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}