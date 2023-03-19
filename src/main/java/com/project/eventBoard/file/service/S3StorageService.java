package com.project.eventBoard.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileDownloadDto;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.project.eventBoard.common.StatusCode.ONLY_IMAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class S3StorageService implements FileStorageService {

    private final FileService fileService;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.domain}")
    private String s3Domain;


    @Transactional
    @Override
    public FileResponseDto upload(MultipartFile file) throws IOException {
        String objectKey = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("");
        
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, objectKey, file.getInputStream(), objectMetadata);

        amazonS3Client.putObject(putObjectRequest);

        GenericFile genericFile = fileService.save(file, s3Domain + objectKey);
        return new FileResponseDto(genericFile);
    }
    
    @Override
    public void delete(Long id){
        FileResponseDto file = fileService.findById(id);

        amazonS3Client.deleteObject(bucket, file.getPath());

        fileService.deleteById(id);
    }
}
