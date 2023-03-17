package com.project.eventBoard.file.service;

import com.project.eventBoard.file.domain.GenericFile;
import com.project.eventBoard.file.domain.dto.FileResponseDto;
import com.project.eventBoard.file.repository.FileRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindException;

import java.io.IOException;

import static com.project.eventBoard.common.StatusCode.ONLY_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FileServiceUnitTest {

    @Mock FileRepository fileRepository;
    @InjectMocks FileServiceLocal fileService;

    @Test @DisplayName("파일 저장 테스트")
    public void save() throws BindException, IOException {
        // given
        String originalName = "test_image.jpeg";
        GenericFile givenFile = GenericFile.builder()
                        .originalName(originalName)
                        .path("/upload/")
                        .fullPath("/static/upload/")
                        .build();
        given(fileRepository.save(any(GenericFile.class)))
                .willReturn(givenFile);

        // when
        byte[] bytes = new byte[1];
//        bytes[0] = (byte) 21;
        MockMultipartFile requestFile = new MockMultipartFile("file", originalName, MediaType.IMAGE_JPEG_VALUE, bytes);
        FileResponseDto responseDto = fileService.upload(requestFile);

        // then
        assertThat(responseDto.getName()).isEqualTo(originalName);
        assertThat(responseDto.getPath()).contains("upload");
    }

    @Test @DisplayName("파일 저장 -> 예외 발생 테스트")
    public void saveThrowBindException() throws IOException, BindException {
        // given
        MockMultipartFile requestFile = new MockMultipartFile("file", "test_file.txt", MediaType.TEXT_PLAIN.getType(), "test content".getBytes());

        // when
        BindException exception = assertThrows(BindException.class, () -> fileService.upload(requestFile));
        assertThat(exception.getAllErrors().get(0).getDefaultMessage()).isEqualTo(ONLY_IMAGE.getMessage());
    }

}
