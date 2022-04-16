package com.project.application.file.service;

import com.project.application.file.domain.dto.FileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileResponseDto upload(MultipartFile file) throws IOException;

    Object download(Long id);
}
