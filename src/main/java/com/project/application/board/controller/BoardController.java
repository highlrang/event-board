package com.project.application.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("boardId", id);
        return "board/detail";
    }

    @GetMapping("/list/{boardType}")
    public String list(@PathVariable("boardType") String boardType,
                       Model model){
        model.addAttribute("boardType", boardType);
        return "board/list";
    }

    @GetMapping("/save")
    public String saveForm(){
        return "board/save";
    }
}
