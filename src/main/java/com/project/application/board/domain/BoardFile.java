package com.project.application.board.domain;

import com.querydsl.core.types.dsl.CaseBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.File;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String path;

    private String name;

    private String fullPath;

    @Builder
    public BoardFile(String originalName, String path, String name, String fullPath){
        this.originalName = originalName;
        this.path = path;
        this.name = name;
        this.fullPath = fullPath;
    }

    public static BoardFile convert(MultipartFile file){
        if(file == null) return null;
        if(file.isEmpty()) return null;

        return BoardFile.builder()
                .originalName(file.getOriginalFilename())
                .build();
    }
}
