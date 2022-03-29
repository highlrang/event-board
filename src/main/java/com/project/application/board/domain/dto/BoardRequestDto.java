package com.project.application.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter @Getter
public class BoardRequestDto {

    @NotEmpty(message = "게시글 종류를 선택해주세요")
    private String boardType;

    @NotNull(message = "사용자에 대한 정보가 없습니다")
    private Long writerId;

    @NotEmpty(message = "제목을 입력해주세요") // blank empty
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "시작일을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료일을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private int recruitingCnt;

    private MultipartFile file;

    public Board toEntity(){
        return Board.builder()
                .boardType(BoardType.valueOf(boardType))
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .recruitingCnt(recruitingCnt)
                .build();
    }


}
