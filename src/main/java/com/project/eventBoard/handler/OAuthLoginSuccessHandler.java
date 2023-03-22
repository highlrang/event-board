package com.project.eventBoard.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project.eventBoard.auth.token.AuthToken;
import com.project.eventBoard.auth.token.AuthTokenProvider;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.project.eventBoard.user.repository.UserRepository;
import com.project.eventBoard.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final UserService userService;
    private final AuthTokenProvider authTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        UserResponseDto user = (UserResponseDto) authentication.getPrincipal();
        
        AuthToken authToken = authTokenProvider.createAuthToken(user.getId(), user.getRole());

        String accessTokenStr = authToken.getToken();

        String refreshTokenStr = user.getRefreshToken();
        if(refreshTokenStr == null){
            refreshTokenStr = authTokenProvider.createRefreshToken(user.getId(), user.getRole()).getToken();
            
        }else{
            AuthToken refreshAuthToken = authTokenProvider.convertAuthToken(refreshTokenStr);
            if(!refreshAuthToken.validate())
                refreshTokenStr = authTokenProvider.createRefreshToken(user.getId(), user.getRole()).getToken();    
                
        }

        userService.updateRefreshToken(user.getId(), refreshTokenStr);

        Cookie accessCookie = new Cookie("token", accessTokenStr);
        Cookie refreshCookie = new Cookie("refresh", refreshTokenStr);
        
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.sendRedirect("/");
    }

    
}
