package com.project.application.user.controller.api;

import com.project.application.common.ApiResponseBody;
import com.project.application.common.StatusCode;
import com.project.application.user.domain.dto.UserRequestDto;
import com.project.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.application.common.StatusCode.SUCCESS;
import static com.project.application.common.StatusCode.USER_ALREADY_EXIST;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/validate-user-id")
    public ResponseEntity<?> validateId(@RequestParam("userId") String userId){
        StatusCode statusCode = !userService.existUserId(userId) ? SUCCESS : USER_ALREADY_EXIST;
        return new ResponseEntity<>(
                new ApiResponseBody<>(statusCode.getCode(), statusCode.getMessage()),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<?> join(@RequestBody UserRequestDto dto){
        userService.join(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
