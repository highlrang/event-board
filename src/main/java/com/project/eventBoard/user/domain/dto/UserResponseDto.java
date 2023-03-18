package com.project.eventBoard.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.eventBoard.auth.dto.KakaoUserInfo;
import com.project.eventBoard.auth.dto.OAuth2UserInfo;
import com.project.eventBoard.user.domain.Role;
import com.project.eventBoard.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor
public class UserResponseDto extends KakaoUserInfo implements UserDetails {

    private Long id;

    private String userId;

    private String nickName;

    @JsonIgnore
    private String password;

    private Role role;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.createdDate = user.getCreatedDate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Collections.singleton(
                new SimpleGrantedAuthority(role.getCode())));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }   
}
