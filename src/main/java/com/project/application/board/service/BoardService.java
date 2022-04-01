package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BoardService {

    BoardResponseDto findById(Long id, Long userId);

    Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable);

    Long save(BoardRequestDto dto);

    void updateAllToBest(List<Long> ids);
}
