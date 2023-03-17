package com.project.eventBoard.file.service;

import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.exception.CustomException;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.project.eventBoard.common.StatusCode.*;
import static com.project.eventBoard.config.WebConfig.staticPath;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceLocal implements FileService{

    private final FileRepository fileRepository;

    @Transactional
    @Override
    public FileResponseDto upload(MultipartFile file) throws IOException, BindException {
        if(file == null || file.isEmpty()) return null;

        /** 검증으로 따로 빼기 */
        if(!file.getContentType().contains("image")){
            BindingResult bindingResult = new BeanPropertyBindingResult(BoardRequestDto.class, "board");
            bindingResult.reject(ONLY_IMAGE.getCode(), ONLY_IMAGE.getMessage());
            throw new BindException(bindingResult);
        }

        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String name = LocalDateTime.now().getNano() + extension;

        String path = "/upload/" + LocalDate.now();
        File dirPath = new File(staticPath + path);
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

    @Override
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

    @Transactional
    @Override
    public void delete(Long id) {
        fileRepository.deleteById(id);
    }
}
