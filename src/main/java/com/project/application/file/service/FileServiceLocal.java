package com.project.application.file.service;

import com.project.application.exception.CustomException;
import com.project.application.file.domain.GenericFile;
import com.project.application.file.domain.dto.FileResponseDto;
import com.project.application.file.repository.FileRepository;
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
import static com.project.application.common.StatusCode.FILE_DOWNLOAD_FAILED;
import static com.project.application.common.StatusCode.FILE_NOT_FOUND;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceLocal implements FileService{

    private final FileRepository fileRepository;

    @Transactional
    @Override
    public FileResponseDto upload(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) return null;

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        String name = LocalDateTime.now().getNano() + extension;

        String path = "/static/upload/" + LocalDate.now();
        File dirPath = new File(FILE_BASE_PATH + path);
        if(!dirPath.exists()) dirPath.mkdirs();

        File uploadFile = new File(dirPath.getAbsolutePath() + "/" + name);
        file.transferTo(uploadFile);

        GenericFile result = fileRepository.save(GenericFile.builder()
                .originalName(originalName)
                .path(path)
                .name(name)
                .fullPath(uploadFile.getAbsolutePath())
                .build()
        );

        return new FileResponseDto(result);
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
        GenericFile file = fileRepository.findById(id)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND.getCode(), FILE_NOT_FOUND.getMessage()));
        try {
            UrlResource urlResource = new UrlResource("file", file.getFullPath());
            return new FileDownloadDto(file.getOriginalName(), urlResource);
        } catch (MalformedURLException e) {
            throw new CustomException(FILE_DOWNLOAD_FAILED.getCode(), FILE_DOWNLOAD_FAILED.getMessage());
        }
    }
}
