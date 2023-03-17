package com.project.eventBoard.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private String statusCode;

    private String message;

    public CustomException(String message){
        super(message);
    }

    public CustomException(String statusCode, String message){
        this.statusCode = statusCode;
        this.message = message;
    }
}
