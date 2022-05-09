package com.project.application.file.service;

import com.project.application.board.domain.dto.BoardRequestDto;
import com.project.application.file.domain.GenericFile;
import com.project.application.file.domain.dto.FileResponseDto;
import com.project.application.file.repository.FileRepository;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.project.application.common.StatusCode.ONLY_IMAGE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceS3 implements FileService {

    private FileRepository fileRepository;

    @Transactional
    @Override
    public FileResponseDto upload(MultipartFile file) {
        return null;
    }

    @Override
    public Object download(Long id) {
        return null;
    }

    @Override
    public void delete(Long id){
        fileRepository.deleteById(id);
    }
}
