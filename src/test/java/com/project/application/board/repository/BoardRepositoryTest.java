package com.project.application.board.repository;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;
    @Autowired UserRepository userRepository;

    @Test @DisplayName("게시글 목록 정렬 테스트") @WithMockUser
    public void pagingWithSort(){
        int page = 0;
        int size = 5;

        User writer = userRepository.save(
                User.builder()
                        .userId("userId")
                        .password("password")
                        .build()
        );

        LocalDate date = LocalDate.now();
        List<Board> boardList = new ArrayList<>();
        for(int i=0; i<10; i++){
            Board board = Board.builder()
                    .boardType(BoardType.event)
                    .title("title")
                    .content("content")
                    .startDate(date)
                    .endDate(date.plusDays(i))
                    .build();
            board.setWriter(writer);
            boardList.add(board);
        }
        boardRepository.saveAll(boardList);

        /** when */
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "endDate"));

        Page<BoardResponseDto> paging =
                boardRepository.findPaging(BoardType.event, pageable);

        /** then */
        assertThat(paging.getTotalElements()).isEqualTo(boardList.size());
        paging.getContent().forEach(
                b -> {
                    assertThat(b.getWriterId()).isEqualTo(writer.getId());
                    System.out.println("Sort endDate DESC 확인 -> " + b.getEndDate());
                }
        );
        assertTrue(paging.getSort().getOrderFor("endDate").isDescending());

    }


}
