package com.project.application.board.controller.api;

import com.project.application.board.domain.dto.BoardResponseDto;
import com.project.application.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/api")
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/list")
    public ResponseEntity<?> list(Pageable pageable){
        return new ResponseEntity<Page<BoardResponseDto>>(boardService.findPaging(pageable), HttpStatus.OK);
    }
}
