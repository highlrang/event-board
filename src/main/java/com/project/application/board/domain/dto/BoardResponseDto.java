package com.project.application.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private BoardType boardType;
    private String title;
    private String content;

    private Long writerId;
    private String writerName;

    private Integer recruitingCnt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    private Boolean isBest;

    @QueryProjection
    public BoardResponseDto(Long id, BoardType boardType, String title, String content,
                            Long writerId, String writerName, Integer recruitingCnt, Boolean isBest,
                            LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdDate) {
        this.id = id;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.writerId = writerId;
        this.writerName = writerName;
        this.recruitingCnt = recruitingCnt;
        this.isBest = isBest;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
    }

    public BoardResponseDto(Board entity){
        this.id = entity.getId();
        this.boardType = entity.getBoardType();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.recruitingCnt = entity.getRecruitingCnt();
        this.isBest = entity.getIsBest();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.createdDate = entity.getCreatedDate();
    }
}
