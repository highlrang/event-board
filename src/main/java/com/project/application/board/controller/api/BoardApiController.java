package com.project.application.board.controller.api;

import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.service.BoardService;
import com.project.application.common.ApiResponseBody;
import com.project.application.common.CreateGroup;
import com.project.application.file.service.FileService;
import com.project.application.file.service.FileServiceLocal;
import com.project.application.user.domain.dto.UserResponseDto;
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

import static com.project.application.common.StatusCode.FILE_SAVE_FAILED;
import static com.project.application.common.StatusCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id,
                                      @AuthenticationPrincipal UserResponseDto user){
        return new ResponseEntity<>(boardService.findById(id, user.getId()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam("boardType") String boardType,
                                  @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponseDto> boardPaging = boardService.findPaging(BoardType.valueOf(boardType), pageable);
        return new ResponseEntity<>(boardPaging, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Validated(value = CreateGroup.class) BoardRequestDto dto) throws BindException{
        try {
            Long id = boardService.save(dto);
            return new ResponseEntity<>(id, HttpStatus.OK);

        } catch (IOException e) {
            /* FILE EXCEPTION -> file controller 로 이동 예정*/
            ApiResponseBody<List<String>> body =
                    new ApiResponseBody<>(FILE_SAVE_FAILED.getCode(), FILE_SAVE_FAILED.getMessage(), Arrays.asList(FILE_SAVE_FAILED.getMessage()));
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long boardId,
                                    @Valid BoardRequestDto boardDto){
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
