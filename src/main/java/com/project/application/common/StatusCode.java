package com.project.application.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusCode {

    NOT_FOUND("1001", "존재하지 않습니다."),
    VALIDATION_EXCEPTION("1002", "검증 오류입니다."),

    /**
     * User
     */
    USER_NOT_FOUND("2001", "사용자를 찾을 수 없습니다."),

    /**
     * Board
     *
     * */
    BOARD_NOT_FOUND("3001", "게시글을 찾을 수 없습니다."),

    /**
     * Registration
     */
    REGISTRATION_NOT_FOUND("4001", "등록 정보를 찾을 수 없습니다.");

    private final String code;
    private final String message;
}
