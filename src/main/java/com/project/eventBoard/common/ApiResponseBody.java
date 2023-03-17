package com.project.eventBoard.common;

import lombok.Getter;

@Getter
public class ApiResponseBody<T> {
    
    private String statusCode;
    private String message;
    private T body;
    
    public ApiResponseBody(String statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }

    public ApiResponseBody(String statusCode, String message, T body){
        this.statusCode = statusCode;
        this.message = message;
        this.body = body;
    }
}
