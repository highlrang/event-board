package com.project.eventBoard.auth.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

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
        Map<String, String> kakaoAcountMap = (Map<String, String>) attributes.get("kakao_account");
        return kakaoAcountMap.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, String> propertyMap = (Map<String, String>) attributes.get("properties");
        return propertyMap.get("nickname").toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    }
    
}
