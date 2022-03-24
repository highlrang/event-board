package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import com.project.application.exception.CustomException;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.application.common.StatusCode.BOARD_NOT_FOUND;
import static com.project.application.common.StatusCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public BoardResponseDto findById(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND.getCode(), BOARD_NOT_FOUND.getMessage()));
        return new BoardResponseDto(board);
    }

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        return boardRepository.findPaging(boardType, pageable);
    }

    @Override
    public Long save(BoardRequestDto dto){
        Board board = dto.toEntity();
        User writer = userRepository.findById(dto.getWriterId())
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
        board.setWriter(writer);
        return boardRepository.save(board).getId();
    }

    @Override
    public void updateAllToBest(List<Long> ids) {
        boardRepository.updateAllToBest(ids);
    }
}
