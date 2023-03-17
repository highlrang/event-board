package com.project.eventBoard.board.repository;

import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable);

    List<BoardResponseDto> findLimitOrderBy(int limit, String field);

    void updateAllToBest(List<Long> ids);
}
