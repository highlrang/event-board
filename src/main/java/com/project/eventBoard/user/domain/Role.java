package com.project.eventBoard.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"), MEMBER("ROLE_MEMBER", "회원");

    private final String code;
    private final String displayName;
}
