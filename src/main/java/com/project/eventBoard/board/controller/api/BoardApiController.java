package com.project.eventBoard.board.controller.api;

import com.project.eventBoard.board.domain.BoardType;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.board.domain.dto.BoardResponseDto;
import com.project.eventBoard.board.service.BoardService;
import com.project.eventBoard.common.ApiResponseBody;
import com.project.eventBoard.common.CreateGroup;
import com.project.eventBoard.file.service.FileService;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static com.project.eventBoard.common.StatusCode.FILE_SAVE_FAILED;
import static com.project.eventBoard.common.StatusCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id,
                                      @AuthenticationPrincipal UserResponseDto user){
        return new ResponseEntity<>(
                new ApiResponseBody<>(
                        SUCCESS.getCode(), SUCCESS.getMessage(),
                        boardService.findById(id, user.getId()))
                , HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam("boardType") String boardType,
                                  @PageableDefault(size = 12, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponseDto> boardPaging = boardService.findPaging(BoardType.valueOf(boardType), pageable);
        return new ResponseEntity<>(boardPaging, HttpStatus.OK);
    }

    @GetMapping("/first-screen")
    public ResponseEntity<?> listNew(@RequestParam("field") String field){
        List<BoardResponseDto> boardList = boardService.findFirstScreenList(10, field);
        return new ResponseEntity<>(boardList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated(value = CreateGroup.class) @RequestBody BoardRequestDto dto) throws BindException {
        Long id = boardService.save(dto);
        return new ResponseEntity<>(new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage(), id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long boardId,
                                    @Valid @RequestBody BoardRequestDto boardDto) throws BindException {
        boardService.update(boardId, boardDto);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage())
                , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage())
                , HttpStatus.OK);
    }
}
