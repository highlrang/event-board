package com.project.application.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.application.user.domain.Role;
import com.project.application.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
public class UserResponseDto implements UserDetails {

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
        return new ArrayList(Collections.singleton(role));
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
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
