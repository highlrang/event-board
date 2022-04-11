package com.project.application.user.service;

import com.project.application.user.domain.dto.UserRequestDto;

public interface UserService {
    Boolean existUserId(String userId);
    Long join(UserRequestDto dto);
}
