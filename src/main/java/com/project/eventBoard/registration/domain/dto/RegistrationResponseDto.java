package com.project.eventBoard.registration.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.registration.domain.RegistrationStatus;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RegistrationResponseDto {

    private Long id;

    private String userEmail;
    private String userName;

    private RegistrationStatus status;
    private String statusName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    @QueryProjection
    public RegistrationResponseDto(Long id, String userEmail, String userName, RegistrationStatus status, LocalDateTime createdDate){
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
        this.status = status;
        this.statusName = status.getName();
        this.createdDate = createdDate;
    }
}
