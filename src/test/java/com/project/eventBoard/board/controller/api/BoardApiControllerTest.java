package com.project.eventBoard.board.controller.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import com.project.eventBoard.board.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class BoardApiControllerTest {
    @MockBean BoardRepository boardRepository;
    @Autowired MockMvc mockMvc;

    /** board 목록
     * 페이지
     * 정렬
     * with querydsl 테스트
     */
    @Test @DisplayName("게시글 목록 테스트") @WithMockUser
    public void list() throws Exception {
        /** given */
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        List<BoardResponseDto> boardList = new ArrayList<>();
        for(int i=1; i<size + 50; i++){
            BoardResponseDto boardResponseDto = new BoardResponseDto();
            boardResponseDto.setWriterId("writerId");
            boardResponseDto.setId((long) i);
            boardList.add(boardResponseDto);
        }

        Page<BoardResponseDto> givenBoardPaging = new PageImpl<>(
                boardList.subList((int) pageable.getOffset(), Math.min(boardList.size(), pageable.getPageSize())),
                pageable,
                boardList.size()
        );

        given(boardRepository.findPaging(any(BoardType.class), any()))
                .willReturn(givenBoardPaging);

        /** when */
        MvcResult mvcResult = mockMvc.perform(get("/api/board")
                        .param("boardType", BoardType.event.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        /** then */
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        Page<BoardResponseDto> result = om.readValue(contentAsString, new TypeReference<CustomPageImpl<BoardResponseDto>>(){});

        assertThat(result.getTotalElements()).isEqualTo(boardList.size());
        assertThat(result.getContent().size()).isEqualTo(size);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(page);
        assertThat(result.getTotalPages()).isEqualTo(boardList.size() < size ? boardList.size() : ((result.getTotalElements() / size) + 1));
    }

}
