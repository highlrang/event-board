package com.project.application.registration.service;

import com.project.application.board.domain.Board;
import com.project.application.board.repository.BoardRepository;
import com.project.application.exception.CustomException;
import com.project.application.registration.domain.Registration;
import com.project.application.registration.domain.dto.RegistrationRequestDto;
import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.project.application.registration.domain.dto.RegistrationUpdateDto;
import com.project.application.registration.repository.RegistrationRepository;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.application.common.StatusCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationServiceImpl implements RegistrationService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final RegistrationRepository registrationRepository;

    @Override
    public List<RegistrationResponseDto> findByBoardId(Long boardId) {
        return registrationRepository.findAllByBoardId(boardId);
    }

    @Transactional
    @Override
    public Long save(RegistrationRequestDto dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND.getCode(), BOARD_NOT_FOUND.getMessage()));
        if(board.isWriter(user.getId()))
            throw new CustomException(REGISTRATION_RESTRICTION.getCode(), REGISTRATION_RESTRICTION.getMessage());
        Registration result = registrationRepository.save(new Registration(user, board));
        return result.getId();
    }

    @Transactional
    @Override
    public void delete(Long id){
        registrationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Long update(RegistrationUpdateDto dto){
        Registration registration = registrationRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(REGISTRATION_UPDATE_FAILED.getCode(), REGISTRATION_UPDATE_FAILED.getMessage()));

        registration.setStatus(dto.getStatus());
        return registration.getBoard().getId();
    }
}
