package com.project.eventBoard.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"), NORMAL("ROLE_NORMAL", "사용자");

    private final String title;
    private final String name;
}
