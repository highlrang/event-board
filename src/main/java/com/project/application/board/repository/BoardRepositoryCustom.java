package com.project.application.board.repository;

import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable);
    void updateAllToBest(List<Long> ids);
}
