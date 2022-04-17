package com.project.application.file.service;

import com.project.application.file.domain.GenericFile;
import com.project.application.file.domain.dto.FileResponseDto;
import com.project.application.file.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FileServiceUnitTest {

    @Mock FileRepository fileRepository;
    @InjectMocks FileServiceLocal fileService;

    @Test @DisplayName("파일 저장 테스트")
    public void save() throws IOException, BindException {

        // given
        MockMultipartFile requestFile = new MockMultipartFile("test_file", "test_file.txt", null, "test content".getBytes());
        GenericFile givenFile = GenericFile.builder()
                        .originalName(requestFile.getOriginalFilename())
                                .fullPath("/upload/")
                                        .build();
        given(fileRepository.save(any(GenericFile.class)))
                .willReturn(givenFile);

        // when
        FileResponseDto responseDto = fileService.upload(requestFile);

        // then
        assertThat(responseDto.getName()).isEqualTo(requestFile.getOriginalFilename());
        assertThat(responseDto.getPath()).contains("/upload");

    }

    @Test @DisplayName("파일 다운로드 테스트")
    public void download(){

    }

}
