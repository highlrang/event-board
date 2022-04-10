package com.project.application.registration.repository;

import com.project.application.registration.domain.dto.RegistrationResponseDto;

import java.util.List;

public interface RegistrationRepositoryCustom {

    /** User 정보까지 꺼내와야하기 때문에 Querydsl in query 사용 */
    List<RegistrationResponseDto> findAllByBoardId(Long boardId);
}

