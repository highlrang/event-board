package com.project.eventBoard.file.service;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.project.eventBoard.common.StatusCode.*;
import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileDownloadDto;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    
    private final FileRepository fileRepository;
    private static final List<String> FILE_TYPE_ARR = Arrays.asList("JPG", "JPEG", "jpg", "jpeg", "PNG", "png", "BMP", "bmp", "GIF", "gif");
    private static final String UPLOAD_PATH = "/upload/";

    public FileResponseDto findById(long id){
        GenericFile file = fileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        return new FileResponseDto(file);
    }

    @Transactional
    public GenericFile save(MultipartFile file, String filePath){

        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);

        GenericFile genericFile = GenericFile.builder()
                .originalName(file.getOriginalFilename())
                .path(UPLOAD_PATH + LocalDate.now())
                .name(LocalDateTime.now().getNano() + extension)
                .fullPath(filePath)
                .build();

        return fileRepository.save(genericFile);
    }

    public String getFileExtension(String fileName){
        String extension = "";
        int idx = fileName.lastIndexOf(".");

        if(idx < 0) return extension;

        extension = fileName.substring(idx);

        if(!FILE_TYPE_ARR.contains(extension))
            extension = "";

        return extension;
    }

    @Transactional
    public void deleteById(long id){
        fileRepository.deleteById(id);
    }

    public FileDownloadDto download(Long id){
        FileResponseDto file = this.findById(id);

        try {
            UrlResource urlResource = new UrlResource("file", file.getFullPath());
            return new FileDownloadDto(file.getName(), urlResource);
            
        } catch (MalformedURLException e) {
            throw new CustomException(FILE_DOWNLOAD_FAILED.getCode(), FILE_DOWNLOAD_FAILED.getMessage());
        }
    }

}
