package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.*;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Void> addSchedule(@RequestBody ScheduleDTO ScheduleDTO, @LoginUser MemberDTO user){
        int isNew = scheduleService.isExistSchedule(ScheduleDTO.getId());
        ScheduleDTO.setMemberId(user.getId());
        if(isNew == 0) { //등록 되어있지 않은 스케줄이라면
            scheduleService.addSchedule(ScheduleDTO);
        }
        else{
            System.out.println("구형");
            scheduleService.updateSchedule(ScheduleDTO);
            scheduleService.deleteScheSpot(ScheduleDTO.getId());
            scheduleService.deleteMoney(ScheduleDTO.getId());
        }
        System.out.println("여기까진 옴");
        List<ScheSpotDTO> schespot = ScheduleDTO.getScheSpot();
        scheduleService.insertScheSpot(schespot);
        System.out.println("돈 넣자");
        List<MoneyDTO> money = ScheduleDTO.getMoney();
        scheduleService.insertMoney(money);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/schespot")
    public ResponseEntity<>


}
