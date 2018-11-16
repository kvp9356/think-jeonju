package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.*;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.ScheduleService;
import com.kvp.thinkjeonju.service.SpotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/schedules")
public class ApiScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SpotService spotService;

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
    public ResponseEntity<Void> addSchedule(@RequestBody ScheduleDTO scheduleDTO, @LoginUser MemberDTO user){
        int isNew = scheduleService.isExistSchedule(scheduleDTO.getId());
        scheduleDTO.setMemberId(user.getId());
        if(isNew == 0) { //등록 되어있지 않은 스케줄이라면
            scheduleService.addSchedule(scheduleDTO);
        }
        else{
            scheduleService.updateSchedule(scheduleDTO);
            scheduleService.deleteScheSpot(scheduleDTO.getId());
            scheduleService.deleteMoney(scheduleDTO.getId());
        }

        scheduleService.insertScheSpot(scheduleDTO.getScheSpot());

        scheduleService.insertMoney(scheduleDTO.getMoney());
        return new ResponseEntity(HttpStatus.CREATED);
}

    @PutMapping("/changewriting")   //스케줄 생성
    public ResponseEntity<Void> changeWriting(@RequestParam(value = "id") String scheduleId, @LoginUser MemberDTO user){
        scheduleService.changeWriting(scheduleId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}/schespot/{schespotId}")
    public ResponseEntity<Void> getSpotData(@PathVariable("scheduleId")  String scheduleId, @RequestParam(value = "date") String date, @PathVariable("schespotId") String spotId, Model model){
        SpotDTO spot = spotService.getSpotDetail(spotId).toDTO();
        spot.setImgUrl(spotService.getSpotImg(spot.getId()));
        spot.setLikeCnt(spotService.getLikeCnt(spot.getId()));
        List<MoneyDTO> money = scheduleService.getSpotMoney(scheduleId,date,spotId);

        model.addAttribute("spot",spot);
        model.addAttribute("money",money);

        return new ResponseEntity(model,HttpStatus.OK);
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<Void> getBeforeScheduleData(@PathVariable String scheduleId, Model model){
        List<ScheSpotDTO> scheSpot = scheduleService.getScheSpotById(scheduleId);
        for(int i =0;i<scheSpot.size();i++){
            scheSpot.get(i).setSpotimg(spotService.getSpotImg(scheSpot.get(i).getSpotId()).get(0));
            scheSpot.get(i).setLikeCnt(spotService.getLikeCnt(scheSpot.get(i).getSpotId()));
            scheSpot.get(i).setMoney(scheduleService.getSpotMoney( scheSpot.get(i).getId(),scheSpot.get(i).getScheDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),scheSpot.get(i).getSpotId()));
            scheSpot.get(i).setSpot(spotService.getSpotDetail(scheSpot.get(i).getSpotId()).toDTO());
        }

        model.addAttribute("spot",scheSpot);
        return new ResponseEntity(model,HttpStatus.OK);
    }


}
