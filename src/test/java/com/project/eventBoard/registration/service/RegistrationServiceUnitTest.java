package com.project.eventBoard.registration.service;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.board.repository.BoardRepository;
import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.registration.domain.RegistrationStatus;
import com.project.eventBoard.registration.domain.dto.RegistrationRequestDto;
import com.project.eventBoard.registration.domain.dto.RegistrationUpdateDto;
import com.project.eventBoard.registration.repository.RegistrationRepository;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.project.eventBoard.common.StatusCode.REGISTRATION_RESTRICTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceUnitTest {

    @Mock UserRepository userRepository;
    @Mock BoardRepository boardRepository;
    @Mock RegistrationRepository registrationRepository;
    @InjectMocks RegistrationServiceImpl registrationService;

    @Test @DisplayName("참여자가 작성자일 경우 저장 실패")
    public void save(){
        // given
        Long boardId = 1L;

        User givenUser = new User();
        givenUser.setId("userId");

        Board givenBoard = Board.builder().build();
        givenBoard.setWriter(givenUser);

        given(userRepository.findById(anyString()))
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

    @Test @DisplayName("등록 정보 업데이트 테스트")
    public void update(){
        // given
        Long mockId = 1L;
        Registration givenRegistration = Registration.builder()
                .board(new Board())
                .user(new User()).build();
        given(registrationRepository.findById(anyLong()))
                .willReturn(Optional.of(givenRegistration));

        // when
        RegistrationUpdateDto givenDto = new RegistrationUpdateDto(mockId, RegistrationStatus.OK);
        registrationService.update(givenDto);

        // then
        assertThat(givenRegistration.getStatus())
                .isEqualTo(RegistrationStatus.OK);
    }


}
