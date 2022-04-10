package com.project.application.registration.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationStatus {

    APPLY("신청"), OK("승인"), INCOMPLETE("미완료"), COMPLETE("완료");

    private final String name;
}
