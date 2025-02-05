package com.example.rechick.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청이 성공하였습니다."),

    USER_NOT_LOGIN(false, 2001, "로그인 되지 않았습니다."),
    USER_NOT_NULL(false, 2002,"유저가 없습니다."),
    USER_EMAIL_NULL(false, 2003, "이메일이 없습니다.");

    private final boolean isSuccess;
    private final Integer code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, Integer code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
