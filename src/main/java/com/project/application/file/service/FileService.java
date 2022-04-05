package com.project.application.file.service;

import com.project.application.board.domain.BoardFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    BoardFile upload(MultipartFile file) throws IOException;

    Object download(Long id);
}
