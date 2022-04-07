package com.project.application.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {
    private Long id;
    private String boardType;
    private String title;
    private String content;

    private Long writerId;
    private String writerName;

    private int recruitingCnt;
    private Long registrationCnt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    private Boolean topFix;
    private int views;
    private String fileName;
    private Long fileId;
    private List<RegistrationResponseDto> registrations;

    /** 목록용 dto */
    @QueryProjection
    public BoardResponseDto(Long id, BoardType boardType, String title,
                            Long writerId, String writerName, int recruitingCnt,
                            Long registrationCnt, Boolean topFix, int views,
                            LocalDate startDate, LocalDate endDate, LocalDateTime createdDate) {
        this.id = id;
        this.boardType = boardType.getName();
        this.title = title;
        this.writerId = writerId;
        this.writerName = writerName;
        this.recruitingCnt = recruitingCnt;
        this.registrationCnt = registrationCnt;
        this.topFix = topFix;
        this.views = views;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
    }

    /** 상세용 dto */
    public BoardResponseDto(Board entity){
        this.id = entity.getId();
        this.boardType = entity.getBoardType().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.recruitingCnt = entity.getRecruitingCnt();
        this.views = entity.getViews();
        this.topFix = entity.getTopFix();
        if(entity.getFile() != null) {
            this.fileName = entity.getFile().getOriginalName();
            this.fileId = entity.getFile().getId();
        }
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.createdDate = entity.getCreatedDate();
        this.registrations = entity.getRegistrations().stream()
                .map(RegistrationResponseDto::new)
                .collect(Collectors.toList());
    }
}
