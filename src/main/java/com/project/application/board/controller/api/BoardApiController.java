package com.project.application.board.controller.api;

import com.project.application.board.domain.BoardType;
import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.service.BoardService;
import com.project.application.common.ApiResponseBody;
import com.project.application.file.service.FileService;
import com.project.application.file.service.FileServiceLocal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import static com.project.application.common.StatusCode.FILE_SAVE_FAILED;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardApiController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id,
                                      @RequestParam(value = "userId", required = false) Long userId){
        try{
            return new ResponseEntity<>(boardService.findById(id, userId), HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file-download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Long fileId){
        FileServiceLocal.FileDownloadDto dto = (FileServiceLocal.FileDownloadDto) fileService.download(fileId);
        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(dto.getFileName()).build().toString());
                })
                .body(dto.getResource());
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam("boardType") String boardType, Pageable pageable){
        Page<BoardResponseDto> boardPaging = boardService.findPaging(BoardType.valueOf(boardType), pageable);
        return new ResponseEntity<>(boardPaging, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid BoardRequestDto dto) throws BindException{
        try {
            Long id = boardService.save(dto);
            return new ResponseEntity<>(id, HttpStatus.OK);

        } catch (IOException e) {
            /* FILE EXCEPTION */
            ApiResponseBody<List<String>> body =
                    new ApiResponseBody(FILE_SAVE_FAILED.getCode(), FILE_SAVE_FAILED.getMessage(), Arrays.asList(FILE_SAVE_FAILED.getMessage()));
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
