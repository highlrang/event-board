package com.project.application.file.domain;

import com.project.application.board.domain.Board;
import com.querydsl.core.types.dsl.CaseBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.project.application.common.Constants.FILE_BASE_PATH;

@Entity @Getter
@NoArgsConstructor
public class GenericFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;

    private String path;

    private String name;

    private String fullPath;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL)
    public Board board;

    @Builder
    public GenericFile(String originalName, String path, String name, String fullPath){
        this.originalName = originalName;
        this.path = path;
        this.name = name;
        this.fullPath = fullPath;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void removeBoard(){
        this.board = null;
    }
}
