package com.project.application.user.service;

import com.project.application.user.domain.dto.UserRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Boolean existUserId(String userId);
    Long join(UserRequestDto dto);
}
