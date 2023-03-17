package com.project.eventBoard.registration.domain.dto;

import com.project.eventBoard.registration.domain.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter @NoArgsConstructor
public class RegistrationUpdateDto {

    @NotNull(message = "등록 정보가 필요합니다.")
    private Long id;

    @NotNull(message = "등록 상태 정보가 필요합니다.")
    private RegistrationStatus status;

    public RegistrationUpdateDto(Long id, RegistrationStatus status){
        this.id = id;
        this.status = status;
    }


}
