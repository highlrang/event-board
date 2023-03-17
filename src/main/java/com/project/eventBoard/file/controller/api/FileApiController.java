package com.project.eventBoard.file.controller.api;

import com.project.eventBoard.common.ApiResponseBody;
import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.service.FileService;
import com.project.eventBoard.file.service.FileServiceLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.project.eventBoard.common.StatusCode.FILE_SAVE_FAILED;
import static com.project.eventBoard.common.StatusCode.SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileApiController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> save(@RequestPart MultipartFile file) throws BindException {
        try {
            FileResponseDto result = fileService.upload(file);
            return new ResponseEntity<>(
                    new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage(), result)
                    , HttpStatus.OK);
        } catch (IOException e) {
            throw new CustomException(FILE_SAVE_FAILED.getCode(), FILE_SAVE_FAILED.getMessage());
        }

    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable("id") Long id){
        FileServiceLocal.FileDownloadDto dto = (FileServiceLocal.FileDownloadDto) fileService.download(id);
        return ResponseEntity.ok()
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    httpHeaders.setContentDisposition(ContentDisposition.builder("attachment")
                            .filename(dto.getFileName(), StandardCharsets.UTF_8).build());
                })
                .body(dto.getResource());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        fileService.delete(id);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage()),
                HttpStatus.OK);
    }
}
