package com.project.application.board.service;

import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired BoardService boardService;

    /**
     * 게시글 저장
     * fileService bean 확인
     * jpa 연관관계 저장 확인
     */
    @Test @DisplayName("게시글 저장 시 파일 기능 작동 확인")
    public void save() throws IOException, BindException {
        /** given */
        BoardRequestDto dto = new BoardRequestDto();
        dto.setBoardType(BoardType.event.toString());
        dto.setTitle("test title");
        dto.setContent("test content");

        User user = userRepository.save(User.builder().build());
        dto.setWriterId(user.getId());

        LocalDate now = LocalDate.now();
        dto.setStartDate(now);
        dto.setEndDate(now);

        MockMultipartFile file = new MockMultipartFile("test_file", "test_file.txt", null, "test_content".getBytes());
        dto.setFile(file);

        Long boardId = boardService.save(dto);


        /** when */
        BoardResponseDto result = boardService.findById(boardId, user.getId());

        /** then */
        assertThat(result.getFileName()).isEqualTo("test_file");
    }

    /** 게시글 목록 기능 테스트
    */

    /** 게시글 목록 정렬 기능 테스트 */
}