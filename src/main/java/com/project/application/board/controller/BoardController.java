package com.project.application.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list")
    public String list(){
        return "board/list";
    }

    @GetMapping("/save")
    public String saveForm(){
        return "board/save";
    }
}
