package com.project.application.file.controller.api;

import com.project.application.exception.CustomException;
import com.project.application.file.domain.dto.FileResponseDto;
import com.project.application.file.service.FileService;
import com.project.application.file.service.FileServiceLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.project.application.common.StatusCode.FILE_SAVE_FAILED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileApiController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> save(@RequestPart MultipartFile file){
        try {
            FileResponseDto result = fileService.upload(file);
            return new ResponseEntity<>(result, HttpStatus.OK);
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
                    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(dto.getFileName()).build().toString());
                })
                .body(dto.getResource());
    }
}
