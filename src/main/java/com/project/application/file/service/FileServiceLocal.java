package com.project.application.file.service;

import com.project.application.board.domain.BoardFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.project.application.common.Constants.FILE_BASE_PATH;

@Service
@Transactional(readOnly = true)
public class FileServiceLocal implements FileService{

    @Override
    public BoardFile upload(MultipartFile file) throws IOException {
        if(file.isEmpty()) return null;

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        String name = LocalDateTime.now().getNano() + extension;

        String path = "/upload/" + LocalDate.now();
        File dirPath = new File(FILE_BASE_PATH + path);
        if(!dirPath.exists()) dirPath.mkdirs();

        File uploadFile = new File(dirPath.getAbsolutePath() + "/" + name);
        file.transferTo(uploadFile);

        return BoardFile.builder()
                .originalName(file.getName())
                .path(path)
                .name(name)
                .fullPath(uploadFile.getAbsolutePath())
                .build();
    }
}
