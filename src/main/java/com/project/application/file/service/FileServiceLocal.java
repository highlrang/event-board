package com.project.application.file.service;

import com.project.application.board.domain.BoardFile;
import com.project.application.exception.CustomException;
import com.project.application.file.repository.BoardFileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.project.application.common.Constants.FILE_BASE_PATH;
import static com.project.application.common.StatusCode.FILE_NOT_FOUND;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceLocal implements FileService{

    private final BoardFileRepository fileRepository;

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

        log.info("=== file service LOCAL ===");

        return BoardFile.builder()
                .originalName(file.getName())
                .path(path)
                .name(name)
                .fullPath(uploadFile.getAbsolutePath())
                .build();
    }

    @Getter
    public static class FileDownloadDto{
        private String fileName;
        private Resource resource;
        public FileDownloadDto(String fileName, Resource resource){
            this.fileName = fileName;
            this.resource = resource;
        }
    }

    public FileDownloadDto download(Long id){
        BoardFile file = fileRepository.findById(id)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND.getCode(), FILE_NOT_FOUND.getMessage()));
        try {
            UrlResource urlResource = new UrlResource("file", file.getFullPath());
            return new FileDownloadDto(file.getOriginalName(), urlResource);
        } catch (MalformedURLException e) {
            throw new CustomException("");
        }
    }
}
