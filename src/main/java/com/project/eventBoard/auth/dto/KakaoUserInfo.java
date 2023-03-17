package com.project.eventBoard.auth.dto;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
        
    }

    @Override
    public String getProviderId() {
        return "kakao";
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
    
}
