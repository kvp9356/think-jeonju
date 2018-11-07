package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @GetMapping("")
    public String schedule(@LoginUser MemberDTO user) { return "schedule";}

}
