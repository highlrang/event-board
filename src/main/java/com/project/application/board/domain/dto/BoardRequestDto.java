package com.project.application.board.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.application.board.domain.Board;
import com.project.application.board.domain.BoardType;
import com.project.application.common.CreateGroup;
import com.project.application.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.project.application.common.StatusCode.VALIDATION_EXCEPTION;

@NoArgsConstructor
@Setter @Getter
public class BoardRequestDto {

    @NotEmpty(message = "게시글 종류를 선택해주세요", groups = CreateGroup.class)
    private String boardType;

    @NotNull(message = "사용자에 대한 정보가 없습니다", groups = CreateGroup.class)
    private Long writerId;

    @NotEmpty(message = "제목을 입력해주세요")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "시작일을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료일을 설정해주세요")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Digits(message = "모집 인원은 숫자여야합니다.", integer = 3, fraction = 0)
    private int recruitingCnt;

    private Long fileId;

    public void validateDate() throws BindException {
        if(startDate.isBefore(endDate) || startDate.equals(endDate)) return;

        BindingResult bindingResult = new BeanPropertyBindingResult(BoardRequestDto.class, "board");
        bindingResult.reject(VALIDATION_EXCEPTION.getCode(), "시작 날짜가 종료 날짜보다 앞서거나 같아야합니다");
        throw new BindException(bindingResult);
    }

    public Board toEntity() throws BindException {
        validateDate();

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
