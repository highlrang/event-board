package com.project.application.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {
    private Long id;
    private BoardType boardType;
    private String title;
    private String content;

    private Long writerId;
    private String writerName;

    private int recruitingCnt;
    private int registrationCnt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;

    private Boolean topFix;
    private int views;
    private String fileName;
    private String filePath;
    private List<RegistrationResponseDto> registrations;

    /** 목록용 dto */
    @QueryProjection
    public BoardResponseDto(Long id, BoardType boardType, String title,
                            Long writerId, String writerName, int recruitingCnt,
                            int registrationCnt, Boolean topFix, int views,
                            LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdDate) {
        this.id = id;
        this.boardType = boardType;
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
        this.boardType = entity.getBoardType();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.recruitingCnt = entity.getRecruitingCnt();
        this.views = entity.getViews();
        this.topFix = entity.getTopFix();
        this.fileName = entity.getFile().getOriginalName();
        this.filePath = entity.getFile().getFullPath();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.createdDate = entity.getCreatedDate();
        this.registrations = entity.getRegistrations().stream()
                .map(RegistrationResponseDto::new)
                .collect(Collectors.toList());
    }
}
