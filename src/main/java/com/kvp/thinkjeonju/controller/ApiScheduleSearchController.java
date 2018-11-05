package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.kvp.thinkjeonju.service.SpotService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/schedule/search")
public class ApiScheduleSearchController {

    @Autowired
    private SpotService spotService;

    @PostMapping("")
    public ArrayList<SpotDTO> getSpotData(@RequestParam(value = "dataValue") String dataValue, HttpSession session) {

        log.debug("[Search] Schedule에서 Spot Data 검색");

        ArrayList<SpotDTO> spots = spotService.getSpotData(dataValue);
        MemberDTO m = (MemberDTO)session.getAttribute("loginUser");

        if(m != null) {
            spots = spotService.setLikeInSpotDTOs(m, spots);
        }

        System.out.println("Spot 찾기 완료!");
        return spots;
    }
}
