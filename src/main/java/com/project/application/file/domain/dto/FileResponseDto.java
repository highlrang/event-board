package com.project.application.file.domain.dto;

import com.project.application.file.domain.GenericFile;
import lombok.Getter;

@Getter
public class FileResponseDto {

    private Long id;

    private String name;

    private String path;

    public FileResponseDto(GenericFile file){
        id = file.getId();
        name = file.getOriginalName();
        path = file.getPath() + "/" + file.getName();
    }
}
