package com.project.application.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BoardType {

    meeting("모임"), event("이벤트");

    private final String name;
}
