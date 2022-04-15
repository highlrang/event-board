package com.project.application.registration.service;

import com.project.application.registration.domain.dto.RegistrationRequestDto;
import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.project.application.registration.domain.dto.RegistrationUpdateDto;

import java.util.List;

public interface RegistrationService {

    List<RegistrationResponseDto> findByBoardId(Long boardId);
    Long save(RegistrationRequestDto dto);
    void delete(Long id);
    Long update(RegistrationUpdateDto dto);
}
