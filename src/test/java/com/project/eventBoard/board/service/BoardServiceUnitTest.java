package com.project.eventBoard.board.service;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import com.project.eventBoard.board.repository.BoardRepository;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
import com.project.eventBoard.file.service.FileStorageService;
import com.project.eventBoard.file.service.LocalStorageService;
import com.project.eventBoard.registration.domain.RegistrationStatus;
import com.project.eventBoard.registration.domain.dto.RegistrationResponseDto;
import com.project.eventBoard.registration.repository.RegistrationRepository;
import com.project.eventBoard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.eventBoard.user.domain.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class BoardServiceUnitTest {
    @Mock UserRepository userRepository;
    @Mock BoardRepository boardRepository;
    @Mock RegistrationRepository registrationRepository;
    @Mock FileRepository fileRepository;
    @Mock FileStorageService fileService;
    @InjectMocks BoardServiceImpl boardService;

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

    @Test @DisplayName("작성자 아닌 사용자가 게시글 상세 접속 시 조회수 증가")
    public void detail(){
        Board board = Board.builder()
                .boardType(BoardType.event)
                .title("test title")
                .content("test content")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .build();
        User givenWriter = new User();
        givenWriter.setId(1L);
        board.setWriter(givenWriter);
        Long boardId = 1L;

        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));
        given(registrationRepository.findAllByBoardId(any())) // null
                .willReturn(new ArrayList<>());

        BoardResponseDto boardDto = boardService.findById(boardId, null);
        int views = boardDto.getViews();

        BoardResponseDto afterBoardDto = boardService.findById(boardId, null);
        int afterViews = afterBoardDto.getViews();

        assertThat(views + 1).isEqualTo(afterViews);
    }

    /** 게시글 상세
     * 작성자가 열람했을 때 사용자 정보 확인
     */
    @Test @DisplayName("게시글 상세 열람자가 작성자일 경우 사용자 정보 확인")
    public void detail_check_user(){
        /** given */
        Long boardId = 1L;
        User givenWriter = new User();
        givenWriter.setId(1L);
        Long userId = givenWriter.getId();

        Board givenBoard = Board.builder().boardType(BoardType.event).build();
        givenBoard.setWriter(givenWriter);

        given(boardRepository.findById(any()))
                .willReturn(Optional.of(givenBoard));
        given(registrationRepository.findAllByBoardId(any()))
                .willReturn(new ArrayList<>());

        /** when */
        BoardResponseDto result = boardService.findById(boardId, userId);

        /** then */
        BoardResponseDto.UserInfo userInfo = result.getUserInfo();
        assertThat(userInfo.getUserId()).isEqualTo(userId);
        assertThat(userInfo.getIsWriter()).isEqualTo(true);
    }

    /** 게시글 상세
     * 작성자가 아닌 일반 사용자가 열람했을 때 사용자 정보 확인
     */
    @Test @DisplayName("게시글 상세 열람자가 작성자가 아닐 경우 사용자 정보 확인")
    public void detail_check_user2(){
        /** given */
        Long boardId = 1L;
        Long userId = 1L;
        Long writerId = 2L;
        User givenWriter = new User();
        givenWriter.setId(writerId);

        Board givenBoard = Board.builder().boardType(BoardType.event).build();
        givenBoard.setWriter(givenWriter);

        given(boardRepository.findById(any()))
                .willReturn(Optional.of(givenBoard));
        given(registrationRepository.findAllByBoardId(any()))
                .willReturn(new ArrayList<>());

        /** when */
        BoardResponseDto result = boardService.findById(boardId, userId);

        /** then */
        BoardResponseDto.UserInfo userInfo = result.getUserInfo();
        assertThat(userInfo.getUserId()).isEqualTo(userId);
        assertThat(userInfo.getIsWriter()).isEqualTo(false);

    }

    /** 게시글 상세
     * 등록 인원 setting 확인
     */
    @Test @DisplayName("게시글 상세 조회 시 등록 인원 정보 확인")
    public void detail_registrations(){
        /** given */
        String mockId = "mockUserId";
        User givenWriter = new User();
        givenWriter.setId(mockId);
        Board givenBoard = Board.builder().boardType(BoardType.event).build();
        givenBoard.setWriter(givenWriter);
        User givenUser = User.builder().email("testEmail").build();
        List<RegistrationResponseDto> givenRegistrations = new ArrayList<>();
        givenRegistrations.add(new RegistrationResponseDto(
                mockId,
                mockId,
                givenUser.getEmail(),
                RegistrationStatus.APPLY,
                LocalDateTime.now()
        ));

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(givenBoard));
        given(registrationRepository.findAllByBoardId(any()))
                .willReturn(givenRegistrations);

        /** when */
        BoardResponseDto result = boardService.findById(mockId, mockId);

        /** then */
        assertThat(result.getUserInfo().getIsRegistered()).isEqualTo(true);
        assertThat(result.getRegistrations().get(0).getUserName()).isEqualTo(givenUser.getUserId());
    }

    /** 게시글 수정 */
    @Test @DisplayName("게시글 수정 테스트")
    public void update() throws BindException {
        /** given */
        Long boardId = 1L;
        LocalDate startDate = LocalDate.now();
        int recruitingCnt = 1;
        
        Board givenBoard = Board.builder()
                .boardType(BoardType.event)
                .recruitingCnt(recruitingCnt)
                .startDate(startDate)
                .build();
        GenericFile givenFile = GenericFile.builder().build();

        given(boardRepository.findById(boardId))
                .willReturn(Optional.of(givenBoard));
        given(fileRepository.findById(anyLong()))
                .willReturn(Optional.of(givenFile));
        doNothing().when(fileRepository).deleteUnnecessary();

        /** when */
        BoardRequestDto updateDto = new BoardRequestDto();
        updateDto.setTitle("수정한 제목");
        updateDto.setRecruitingCnt(givenBoard.getRecruitingCnt() + 1);
        updateDto.setStartDate(givenBoard.getStartDate().plusDays(1L));
        LocalDate today = LocalDate.now();
        updateDto.setStartDate(today);
        updateDto.setEndDate(today);
        Long fileId = 1L;
        updateDto.setFileId(fileId);

        boardService.update(boardId, updateDto);
        
        /** then */
        assertThat(givenBoard.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(givenBoard.getRecruitingCnt()).isEqualTo(updateDto.getRecruitingCnt());
        assertThat(givenBoard.getStartDate()).isEqualTo(updateDto.getStartDate());
    }

}
