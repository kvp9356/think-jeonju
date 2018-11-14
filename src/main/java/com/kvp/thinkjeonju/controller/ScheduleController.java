package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @GetMapping("{scheduleId}/detail")
    public String scheduleDetail(@PathVariable String ScheduleId){
        return "1";
    }

    @GetMapping("")
    public String schedule(@LoginUser MemberDTO user) { return "schedule";}

}
