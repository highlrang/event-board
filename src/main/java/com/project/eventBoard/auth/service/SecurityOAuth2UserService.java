package com.project.eventBoard.auth.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.eventBoard.user.domain.Role;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.domain.dto.UserRequestDto;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.project.eventBoard.user.repository.UserRepository;
import com.project.eventBoard.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // if kakao return KakaoUser
        
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();


        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientId = clientRegistration.getClientId();
         
        Boolean existUser = userService.existUserId(clientId);

        if(!existUser){
            userService.join(
                UserRequestDto.builder()
                    .userId(clientId)
                    .password(null)
                    .nickName(oauth2User.getName())
                    .role(oauth2User.getAttribute("role"))
                    .build()
            );

        }

        UserResponseDto userResponseDto = userService.findByUserId(clientId);
        return userResponseDto;

    }

    
}
