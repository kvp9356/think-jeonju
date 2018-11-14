package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.model.Schedule;
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

    @GetMapping("{scheduleId}/detail")
    public String scheduleDetail(@PathVariable String scheduleId, Model model, HttpSession session){
        System.out.println("매핑 왔음따");
        ScheduleDTO schedule = scheduleService.getScheduleById(scheduleId);
        List<MoneyDTO> money = scheduleService.getMoneyById(scheduleId);
        List<ScheSpotDTO> scheSpot = scheduleService.getScheSpotById(scheduleId);
        System.out.println(schedule.getId());
        for(int i=0; i<scheSpot.size();i++) {
            System.out.println(scheSpot.get(i).getSpotId());
        }
        for(int i=0; i<money.size();i++) {
            System.out.println(money.get(i).getName());
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("scheSpot", scheSpot);
        model.addAttribute("money", money);

        if(schedule.getIsWriting()==1){             //완성된것이라면
            return "scheduleComplete";
        }
        else{                                       //미완성이라면
            return "scheduleEdit";
        }

    }

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
