package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.PageDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/myPage")
public class ApiMyPageController {

    @Autowired
    private MyPageService myPageService;

    @GetMapping("/spots/like/page/{pageNo}")
    public ResponseEntity<PageDTO<SpotDTO>> getLikeSpots(@LoginUser MemberDTO member, @PathVariable int pageNo) {
        log.debug("[마이페이지] 회원의 좋아요 누른 장소 리스트 조회");
        return new ResponseEntity(myPageService.getLikeSpotsByPageNo(member, pageNo), HttpStatus.OK);
    }

    @GetMapping("/schedules/like/page/{pageNo}")
    public ResponseEntity<PageDTO<ScheduleDTO>> getLikeSchedules(@LoginUser MemberDTO member, @PathVariable int pageNo) {
        log.debug("[마이페이지] 회원의 좋아요 누른 스케줄 리스트 조회");
        return new ResponseEntity(myPageService.getLikeSchedulesByPageNo(member, pageNo), HttpStatus.OK);
    }

    @GetMapping("/schedules/page/{pageNo}")
    public ResponseEntity<PageDTO<ScheduleDTO>> getPersonalSchedules(@LoginUser MemberDTO member, @PathVariable int pageNo) {
        log.debug("[마이페이지] 회원이 작성한 스케줄 리스트 조회");
        return new ResponseEntity(myPageService.getPersonalSchedulesByPageNo(member, pageNo), HttpStatus.OK);
    }
}
