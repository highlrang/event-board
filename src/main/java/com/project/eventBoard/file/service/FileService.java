package com.project.eventBoard.file.service;

import com.project.eventBoard.file.domain.dto.FileResponseDto;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponseDto upload(MultipartFile file) throws IOException, BindException;

    Object download(Long id);

    void delete(Long id);
}
