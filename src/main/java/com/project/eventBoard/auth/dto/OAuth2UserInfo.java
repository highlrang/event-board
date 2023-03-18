package com.project.eventBoard.auth.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo extends OAuth2User{
    Map<String, Object> getAttributes();
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
