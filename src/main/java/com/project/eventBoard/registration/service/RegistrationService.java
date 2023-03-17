package com.project.eventBoard.registration.service;

import com.project.eventBoard.registration.domain.dto.RegistrationRequestDto;
import com.project.eventBoard.registration.domain.dto.RegistrationResponseDto;
import com.project.eventBoard.registration.domain.dto.RegistrationUpdateDto;

import java.util.List;

public interface RegistrationService {

    List<RegistrationResponseDto> findByBoardId(Long boardId);
    Long save(RegistrationRequestDto dto);
    void saveAll(List<RegistrationRequestDto> dtos);
    void delete(Long id);
    Long update(RegistrationUpdateDto dto);
}
