package com.kvp.thinkjeonju.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @GetMapping("/")
    public String schedule() { return "schedule";}
}
