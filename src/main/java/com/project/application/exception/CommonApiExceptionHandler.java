package com.project.application.exception;

import com.project.application.common.ApiResponseBody;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.application.common.StatusCode.*;

@RestControllerAdvice
public class CommonApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public ApiResponseBody<?> CustomException(CustomException e){
        return new ApiResponseBody<>(e.getStatusCode(), e.getMessage());
    }

    @ExceptionHandler(value = {BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponseBody<?> validationException(BindException e){
        List<String> body = e.getAllErrors().stream()
                .map(fe -> fe.getDefaultMessage())
                .collect(Collectors.toList());

        return new ApiResponseBody<>(VALIDATION_EXCEPTION.getCode(), VALIDATION_EXCEPTION.getMessage(), body);
    }
}
