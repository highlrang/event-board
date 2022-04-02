package com.project.application.file.service;

import com.project.application.board.domain.BoardFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    // entity 반환하지 않는 방향으로 수정하기
    BoardFile upload(MultipartFile file) throws IOException;
}
