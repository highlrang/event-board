package com.project.eventBoard.board.service;

import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
import com.project.eventBoard.user.domain.User;
import com.project.eventBoard.user.repository.UserRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired FileRepository fileRepository;
    @Autowired BoardService boardService;

    @Test @DisplayName("게시글 저장 시 파일 연관관계 매핑 확인(저장 완료된 파일 아이디 given)")
    public void save() throws IOException, BindException {
        /** given */
        BoardRequestDto dto = new BoardRequestDto();
        dto.setBoardType(BoardType.event.toString());
        dto.setTitle("test title");
        dto.setContent("test content");

        User user = userRepository.save(User.builder().email("test").password("test").build());
        dto.setWriterId(user.getId());

        LocalDate now = LocalDate.now();
        dto.setStartDate(now);
        dto.setEndDate(now);

        String fileName = "test_file_name.txt";
        GenericFile file = fileRepository.save(GenericFile.builder()
                        .originalName(fileName)
                        .build());
        dto.setFileId(file.getId());

        /** when */
        Long boardId = boardService.save(dto);
        BoardResponseDto result = boardService.findById(boardId, user.getId());

        /** then */
        assertThat(result.getFile().getId()).isEqualTo(file.getId());
        assertThat(result.getFile().getName()).isEqualTo(fileName);
    }
}