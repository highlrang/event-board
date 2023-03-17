package com.project.eventBoard.file.service;

import com.project.eventBoard.board.domain.dto.BoardRequestDto;
import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
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

import static com.project.eventBoard.common.StatusCode.ONLY_IMAGE;

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
