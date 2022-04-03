package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardFile;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import com.project.application.file.service.FileService;
import com.project.application.file.service.FileServiceLocal;
import com.project.application.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.application.user.domain.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {
    @Mock UserRepository userRepository;
    @Mock BoardRepository boardRepository;
    @InjectMocks BoardServiceImpl boardService;
    @Mock FileServiceLocal fileService;

    @Test @DisplayName("게시글 저장 시 데이터 검증 테스트")
    public void requestDto(){
        BoardRequestDto dto = new BoardRequestDto();
        dto.setBoardType(BoardType.event.getName());
        dto.setWriterId(1L);
        dto.setTitle("test tile");
        dto.setContent("test content");
        LocalDate givenDate = LocalDate.now();
        dto.setStartDate(
                LocalDate.parse(
                        givenDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
        );
        dto.setEndDate(
                LocalDate.parse(
                        givenDate.minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
        );

        BindException exception = assertThrows(BindException.class,
                dto::toEntity);
        assertThat(exception.getAllErrors().get(0).getDefaultMessage())
                .isEqualTo("시작 날짜가 종료 날짜보다 앞서거나 같아야합니다");

    }


    @Test @DisplayName("게시글 저장 시 파일 저장 기능까지")
    public void save() throws IOException, BindException {
        /** given */
        Board givenBoard = Board.builder().build();
        User givenWriter = new User();
        givenBoard.setWriter(givenWriter);

        MockMultipartFile requestFile = new MockMultipartFile("test_file", "test_file.txt", null, "test content".getBytes());
        BoardFile givenFile = BoardFile.builder()
                .originalName(requestFile.getName())
                .fullPath("/upload/")
                .build();
        givenBoard.setFile(givenFile);

        Long mockId = 1L;
        given(boardRepository.save(any(Board.class)))
                .willReturn(givenBoard);
        given(userRepository.findById(any()))
                .willReturn(Optional.of(givenWriter));
        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(givenBoard));
        given(fileService.upload(any(MockMultipartFile.class))) // requestFile
                .willReturn(givenFile);

        /** when */
        BoardRequestDto dto = new BoardRequestDto();
        dto.setBoardType(BoardType.event.toString());
        dto.setTitle("test title");
        dto.setContent("test content");
        dto.setWriterId(mockId);
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now());
        dto.setFile(requestFile);
        boardService.save(dto);

        /** then */
        BoardResponseDto result = boardService.findById(mockId, mockId);
        assertThat(result.getFileName()).isEqualTo("test_file");
        assertThat(result.getFilePath()).contains("/upload/");
    }

    @Test @DisplayName("작성자 아닌 사용자가 게시글 상세 접속 시 조회수 증가")
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

        BoardResponseDto boardDto = boardService.findById(boardId, null);
        int views = boardDto.getViews();

        BoardResponseDto afterBoardDto = boardService.findById(boardId, null);
        int afterViews = afterBoardDto.getViews();

        assertThat(views + 1).isEqualTo(afterViews);
    }


}
