package com.project.application.registration.service;

import com.project.application.board.domain.Board;
import com.project.application.board.repository.BoardRepository;
import com.project.application.exception.CustomException;
import com.project.application.registration.domain.dto.RegistrationRequestDto;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.project.application.common.StatusCode.REGISTRATION_RESTRICTION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceUnitTest {

    @Mock UserRepository userRepository;
    @Mock BoardRepository boardRepository;
    @InjectMocks RegistrationServiceImpl registrationService;

    @Test @DisplayName("참여자가 작성자일 경우 저장 실패")
    public void save(){
        // given
        Long boardId = 1L;

        User givenUser = new User();
        givenUser.testIdSetting();

        Board givenBoard = Board.builder().build();
        givenBoard.setWriter(givenUser);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(givenUser));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(givenBoard));

        RegistrationRequestDto dto = new RegistrationRequestDto();
        dto.setBoardId(boardId);
        dto.setUserId(givenUser.getId());

        // when - then
        assertThatThrownBy(() -> registrationService.save(dto))
                .isInstanceOf(CustomException.class)
                .hasMessage(REGISTRATION_RESTRICTION.getMessage());
    }



}
