package com.project.eventBoard.registration.repository;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.board.repository.BoardRepository;
import com.project.eventBoard.board.service.BoardService;
import com.project.eventBoard.registration.domain.Registration;
import com.project.eventBoard.registration.domain.RegistrationStatus;
import com.project.eventBoard.registration.domain.dto.RegistrationResponseDto;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RegistrationRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired RegistrationRepository registrationRepository;

    @Test @DisplayName("게시글 id에 해당하는 등록 목록 호출 테스트")
    public void listByBoard() throws BindException, IOException {
        // given
        User writer = userRepository.save(User.builder().userId("writer").password("test1234").build());

        BoardRequestDto boardDto = new BoardRequestDto();
        boardDto.setBoardType(BoardType.event.toString());
        boardDto.setTitle("title");
        boardDto.setContent("content");
        boardDto.setWriterId(writer.getId());
        LocalDate date = LocalDate.now();
        boardDto.setStartDate(date);
        boardDto.setEndDate(date);
        Long boardId = boardService.save(boardDto);

        Board board = boardRepository.findById(boardId).get();

        for(int i=0; i<10; i++) {
            User user = userRepository.save(User.builder().userId("test user " + i).password("test1234").build());
            registrationRepository.save(
                    Registration.builder().user(user).board(board).build()
            );
        }

        // when
        List<RegistrationResponseDto> result = registrationRepository.findAllByBoardId(board.getId());

        // then
        assertThat(result.size()).isEqualTo(10);
        result.forEach(r -> {
            assertThat(r.getUserName()).contains("test user");
            assertThat(r.getStatus()).isEqualTo(RegistrationStatus.APPLY);
        });

    }
}
