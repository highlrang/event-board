package com.project.application.registration.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.registration.domain.Registration;
import com.project.application.registration.domain.RegistrationStatus;
import com.project.application.user.domain.dto.UserResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegistrationResponseDto {

    private Long id;

    private Long userId;
    private String userName;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @QueryProjection
    public RegistrationResponseDto(Long id, Long userId, String userName, RegistrationStatus status, LocalDateTime createdDate){
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.status = status.getName();
        this.createdDate = createdDate;
    }
}
