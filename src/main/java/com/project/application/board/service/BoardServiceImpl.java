package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    @Override
    public Page<?> findPaging(Pageable pageable){
        return boardRepository.findPaging(pageable);
    }

    @Override
    public void updateAllToBest(List<Long> ids) {
        boardRepository.updateAll(ids);
    }
}
