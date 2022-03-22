package com.project.application.board.domain.dto;

import com.project.application.board.domain.Board;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private Boolean isBest;

    @QueryProjection
    public BoardResponseDto(String title, String content, LocalDateTime createdDate, Boolean isBest){
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.isBest = isBest;
    }

    public BoardResponseDto(Board entity){
        this.id = entity.getId();
        this.isBest = entity.getIsBest();
    }
}
