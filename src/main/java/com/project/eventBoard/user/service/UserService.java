package com.project.eventBoard.user.service;

import com.project.eventBoard.user.domain.dto.UserRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Boolean existUserId(String userId);
    Long join(UserRequestDto dto);
}
