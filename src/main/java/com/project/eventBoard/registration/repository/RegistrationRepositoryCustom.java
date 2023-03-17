package com.project.eventBoard.registration.repository;

import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.registration.domain.dto.RegistrationRequestDto;
import com.project.eventBoard.registration.domain.dto.RegistrationResponseDto;

import java.util.List;

public interface RegistrationRepositoryCustom {

    /** User 정보까지 꺼내와야하기 때문에 Querydsl in query 사용 */
    List<RegistrationResponseDto> findAllByBoardId(Long boardId);

    void saveAll(List<RegistrationRequestDto> dtos);
}

