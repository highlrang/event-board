package com.project.application.registration.controller.api;

import com.project.application.common.ApiResponseBody;
import com.project.application.registration.domain.dto.RegistrationRequestDto;
import com.project.application.registration.domain.dto.RegistrationResponseDto;
import com.project.application.registration.domain.dto.RegistrationUpdateDto;
import com.project.application.registration.service.RegistrationService;
import com.project.application.user.domain.dto.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.project.application.common.StatusCode.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationApiController {

    private final RegistrationService registrationService;

    @Getter
    static class RegistrationBody{
        private Long id;
        private List<RegistrationResponseDto> registrations;

        public RegistrationBody(List<RegistrationResponseDto> registrations, Long id){
            this.registrations = registrations;
            this.id = id;
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody RegistrationRequestDto dto){
        Long registrationId = registrationService.save(dto);
        List<RegistrationResponseDto> registrations = registrationService.findByBoardId(dto.getBoardId());
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage(),
                        new RegistrationBody(registrations, registrationId))
                , HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                    @RequestParam("boardId") Long boardId,
                                    @AuthenticationPrincipal UserResponseDto user){
        registrationService.delete(id);
        List<RegistrationResponseDto> registrations = registrationService.findByBoardId(boardId);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage(),
                        new RegistrationBody(registrations, user.getId()))
                , HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<?> update(@Valid @RequestBody RegistrationUpdateDto dto){
        Long boardId = registrationService.update(dto);
        List<RegistrationResponseDto> registrations = registrationService.findByBoardId(boardId);
        return new ResponseEntity<>(
                new ApiResponseBody<>(SUCCESS.getCode(), SUCCESS.getMessage(), registrations)
                , HttpStatus.OK);
    }
}
