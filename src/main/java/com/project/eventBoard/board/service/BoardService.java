package com.project.eventBoard.board.service;

import com.project.eventBoard.board.domain.Board;
import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    BoardResponseDto findById(Long id, String userId);

    Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable);

    List<BoardResponseDto> findFirstScreenList(int limit, String field);

    Long save(BoardRequestDto dto) throws BindException;

    void update(Long id, BoardRequestDto dto) throws BindException;

    void delete(Long id);

    void updateAllToBest(List<Long> ids);
}
