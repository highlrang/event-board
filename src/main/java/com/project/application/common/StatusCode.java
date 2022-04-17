package com.project.application.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusCode {

    SUCCESS("1001", "성공"),
    NOT_FOUND("1002", "존재하지 않습니다."),
    VALIDATION_EXCEPTION("1003", "검증 오류입니다."),

    /**
     * User
     */
    USER_NOT_FOUND("2001", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXIST("2002", "이미 존재하는 사용자 이름입니다."),
    LOGIN_FAILED("2003", "로그인에 실패하였습니다."),
    PASSWORD_NOT_CORRECT("2004", "비밀번호를 확인하여 주세요."),
    LOGIN("2005", "로그인 완료"),
    ANONYMOUS("2006", "로그인 미완료"),

    /**
     * Board
     * */
    BOARD_NOT_FOUND("3001", "게시글을 찾을 수 없습니다."),

    /**
     * file
     */
    FILE_NOT_FOUND("4001", "파일을 찾을 수 없습니다."),
    FILE_SAVE_FAILED("4002", "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD_FAILED("4003", "파일 다운로드에 실패했습니다."),
    ONLY_IMAGE("4004", "이미지만 업로드가 가능합니다."),

    /**
     * Registration
     */
    REGISTRATION_NOT_FOUND("5001", "등록 정보를 찾을 수 없습니다."),
    REGISTRATION_FAILED("5002", "참여가 진행되지 않았습니다."),
    REGISTRATION_RESTRICTION("5003", "참여가 제한되었습니다."),
    ALREADY_REGISTERED("5004", "이미 참여하였습니다."),
    REGISTRATION_UPDATE_FAILED("5005", "등록 정보 업데이트에 실패하였습니다.");

    private final String code;
    private final String message;
}
