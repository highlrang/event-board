package com.project.application.registration.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class RegistrationRequestDto {

    @NotNull(message = "사용자 정보가 누락되었습니다.")
    private Long userId;

    @NotNull(message = "게시글 정보가 누락되었습니다.")
    private Long boardId;
}
