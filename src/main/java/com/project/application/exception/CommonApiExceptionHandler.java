package com.project.application.exception;

import com.project.application.common.ApiResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.project.application.common.StatusCode.BOARD_NOT_FOUND;
import static com.project.application.common.StatusCode.NOT_FOUND;

@RestControllerAdvice
public class CommonApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResponseBody<?> CustomException(CustomException e){
        return new ApiResponseBody<>(e.getStatusCode(), e.getMessage());
    }
}
