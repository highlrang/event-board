package com.project.eventBoard.user.service;

import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.domain.dto.UserRequestDto;
import com.project.eventBoard.user.domain.dto.UserResponseDto;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService{
    Boolean existById(String id);
    UserResponseDto findByUserId(String userId);
    String join(UserRequestDto dto);
}
