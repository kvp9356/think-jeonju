package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.ScheduleService;
import com.kvp.thinkjeonju.support.Paging;
import com.kvp.thinkjeonju.support.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("")
    public String scheduleList(@RequestParam(required = false, defaultValue = "1") int page, Model model, HttpSession session) {
        log.debug("[Schedule] Schedule {} 페이지 조회", page);
        Paging paging = scheduleService.getPaging(page);
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByPageNo(paging);
        // 로그인 유저인 경우, 좋아요 표시
        MemberDTO user = (MemberDTO)session.getAttribute(SessionUtil.LOGIN_USER);
        if(user != null) {
            schedules = scheduleService.setLike(user, schedules);
        }

        model.addAttribute("schedules", schedules);
        model.addAttribute("paging", paging);
        return "scheduleList";
    }

    @GetMapping("/create")
    public String createSchedule(@LoginUser MemberDTO user) { return "schedule";}
}
