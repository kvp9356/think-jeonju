package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/schedules")
public class ApiScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/{scheduleId}/like")
    public ResponseEntity<Integer> setScheduleLike(@PathVariable String scheduleId, @LoginUser MemberDTO user) {
        log.debug("[좋아요] {} 스케쥴 좋아요 추가", scheduleId);
        return new ResponseEntity(scheduleService.setScheduleLike(new LikeToDTO(user.getId(), scheduleId, 'c')), HttpStatus.OK);
    }

    @DeleteMapping("/{scheduleId}/like")
    public ResponseEntity<Integer> cancelScheduleLike(@PathVariable String scheduleId, @LoginUser MemberDTO user) {
        log.debug("[좋아요] {} 스케쥴 좋아요 취소", scheduleId);
        return new ResponseEntity(scheduleService.cancelScheduleLike(new LikeToDTO(user.getId(), scheduleId, 'c')), HttpStatus.OK);
    }

    @PostMapping("/{scheduleId}")   //스케줄 생성
    public ResponseEntity<Void> addSchedule(@RequestBody ScheduleDTO ScheduleDTO,@LoginUser MemberDTO user){
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
