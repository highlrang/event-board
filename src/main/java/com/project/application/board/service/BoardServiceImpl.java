package com.project.application.board.service;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.repository.BoardRepository;
import com.project.application.exception.CustomException;
import com.project.application.file.domain.GenericFile;
import com.project.application.file.repository.FileRepository;
import com.project.application.file.service.FileService;
import com.project.application.registration.repository.RegistrationRepository;
import com.project.application.user.domain.User;
import com.project.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.io.IOException;
import java.util.List;

import static com.project.application.common.StatusCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final RegistrationRepository registrationRepository;
    private final FileRepository fileRepository;

    @Override
    public BoardResponseDto findById(Long id, Long userId){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND.getCode(), BOARD_NOT_FOUND.getMessage()));
        if(!board.isWriter(userId)) board.increaseViews();

        BoardResponseDto dto = new BoardResponseDto(board);
        dto.setRegistrations(registrationRepository.findAllByBoardId(dto.getId()));
        dto.setUserInfo(userId);
        return dto;
    }

    @Override
    public Page<BoardResponseDto> findPaging(BoardType boardType, Pageable pageable){
        return boardRepository.findPaging(boardType, pageable);
    }

    @Override
    public List<BoardResponseDto> findFirstScreenList(int limit, String field) {
        return boardRepository.findLimitOrderBy(limit, field);
    }

    @Transactional
    @Override
    public Long save(BoardRequestDto dto) throws BindException {
        Board board = dto.toEntity();

        /** 작성자 매핑 */
        User writer = userRepository.findById(dto.getWriterId())
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
        board.setWriter(writer);

        /** 첨부파일 매핑 */
        if(dto.getFileId() != null) {
            GenericFile file = fileRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new CustomException(FILE_NOT_FOUND.getCode(), FILE_NOT_FOUND.getMessage()));
            board.setFile(file);
        }

        Board result = boardRepository.save(board);
        return result.getId();
    }

    @Transactional
    @Override
    public void update(Long id, BoardRequestDto dto) throws BindException {
        dto.validateDate();

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND.getCode(), BOARD_NOT_FOUND.getMessage()));
        board.update(dto.getTitle(), dto.getContent(), dto.getRecruitingCnt(), dto.getStartDate(), dto.getEndDate());

        /** board - file 연관관계 */
        if(dto.getFileId() == null && board.getFile() != null) {
            board.removeFile();

        }else if(dto.getFileId() != null){
            GenericFile file = fileRepository.findById(dto.getFileId())
                    .orElseThrow(() -> new CustomException(FILE_NOT_FOUND.getCode(), FILE_NOT_FOUND.getMessage()));
            board.setFile(file);
        }

        /** 사용하지 않는 파일 삭제 */
        fileRepository.deleteUnnecessary();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateAllToBest(List<Long> ids) {
        boardRepository.updateAllToBest(ids);
    }
}
