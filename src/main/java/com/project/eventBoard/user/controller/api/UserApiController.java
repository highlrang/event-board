package com.project.eventBoard.user.controller.api;

import com.project.eventBoard.common.ApiResponseBody;
import com.project.eventBoard.common.StatusCode;
import com.project.eventBoard.user.domain.dto.UserRequestDto;
import com.project.eventBoard.user.domain.dto.UserResponseDto;
import com.project.eventBoard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.eventBoard.common.StatusCode.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> info(@AuthenticationPrincipal UserResponseDto user){
        ApiResponseBody<?> body = user == null?
                new ApiResponseBody<>(ANONYMOUS.getCode(), ANONYMOUS.getMessage())
                : new ApiResponseBody<>(LOGIN.getCode(), LOGIN.getMessage(), user);

        return new ResponseEntity<>(
                body,
                HttpStatus.OK);
    }

    @GetMapping("/validate-user-id")
    public ResponseEntity<?> validateId(@RequestParam("userId") String userId){
        StatusCode statusCode = !userService.existById(userId) ? SUCCESS : USER_ALREADY_EXIST;
        return new ResponseEntity<>(
                new ApiResponseBody<>(statusCode.getCode(), statusCode.getMessage()),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<?> join(@Valid @RequestBody UserRequestDto dto){
        userService.join(dto);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage())
                , HttpStatus.OK);
    }
}
