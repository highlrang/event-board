package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.application.user.domain.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {
    @Mock BoardRepository boardRepository;
    @InjectMocks BoardServiceImpl boardService;

    @Test @DisplayName("상세 접속 시 조회수 증가")
    public void detail(){
        Board board = Board.builder()
                .boardType(BoardType.event)
                .title("test title")
                .content("test content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
        board.setWriter(User.builder().build());
        Long boardId = 1L;

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        BoardResponseDto boardDto = boardService.findById(boardId);
        int views = boardDto.getViews();

        BoardResponseDto afterBoardDto = boardService.findById(boardId);
        int afterViews = afterBoardDto.getViews();

        assertThat(views + 1).isEqualTo(afterViews);
    }


}
