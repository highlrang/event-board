package com.project.eventBoard.file.domain.dto;

import com.project.eventBoard.file.domain.GenericFile;
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

    public FileResponseDto(Long id, String name, String path){
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
