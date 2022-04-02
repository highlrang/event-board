package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardFile;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import com.project.application.exception.CustomException;
import com.project.application.file.service.FileService;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
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
    private final FileService fileService;

    @Override
    public BoardResponseDto findById(Long id, Long userId){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND.getCode(), BOARD_NOT_FOUND.getMessage()));
        if(!board.isWriter(userId)) board.increaseViews();
        return new BoardResponseDto(board);
    }

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        return boardRepository.findPaging(boardType, pageable);
    }

    @Override
    public Long save(BoardRequestDto dto) throws IOException {
        Board board = dto.toEntity();

        /** 작성자 매핑 */
        User writer = userRepository.findById(dto.getWriterId())
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
        board.setWriter(writer);

        /** 첨부파일 매핑 */
        board.setFile(fileService.upload(dto.getFile()));

        return boardRepository.save(board).getId();
    }

    @Transactional
    @Override
    public void updateAllToBest(List<Long> ids) {
        boardRepository.updateAllToBest(ids);
    }
}
