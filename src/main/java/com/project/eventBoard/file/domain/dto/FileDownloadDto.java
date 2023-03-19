package com.project.eventBoard.file.domain.dto;

import org.springframework.core.io.Resource;

import lombok.Getter;

@Getter
public class FileDownloadDto {
    
    private String fileName;
    private Resource resource;
    
    public FileDownloadDto(String fileName, Resource resource){
        this.fileName = fileName;
        this.resource = resource;
        
    }
}
