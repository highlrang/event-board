package com.project.application.board.domain.dto;

import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter @Getter
public class BoardRequestDto {

    @NotNull
    private BoardType boardType;

    @NotNull
    private Long writerId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    private int recruitingCnt;

    private MultipartFile file;

    public Board toEntity(){
        return Board.builder()
                .boardType(boardType)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }


}
