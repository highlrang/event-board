package com.project.eventBoard.user.controller;

import com.project.eventBoard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/join")
    public String joinForm(){
        return "user/join";
    }
}
