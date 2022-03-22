package com.project.application.board.service;

import com.project.application.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {

    Page<?> findPaging(Pageable pageable);

    void updateAllToBest(List<Long> ids);

}
