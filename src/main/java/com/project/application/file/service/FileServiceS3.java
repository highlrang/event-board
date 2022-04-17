package com.project.application.file.service;

import com.project.application.file.domain.dto.FileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceS3 implements FileService {

    @Override
    public FileResponseDto upload(MultipartFile file) throws IOException {
        log.info("=== file service AWS S3 ===");

        return null;
    }

    @Override
    public Object download(Long id) {
        return null;
    }

    @Override
    public void delete(Long id){}
}
