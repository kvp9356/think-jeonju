package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.model.Schedule;
import com.kvp.thinkjeonju.repository.MoneyMapper;
import com.kvp.thinkjeonju.repository.ScheSpotMapper;
import com.kvp.thinkjeonju.repository.LikeToMapper;
import com.kvp.thinkjeonju.repository.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ScheSpotMapper scheSpotMapper;

    @Autowired
    private MoneyMapper moneyMapper;

    @Autowired
    private LikeToMapper likeToMapper;


    public List<ScheduleDTO> getBestSchedules() {
        return scheduleMapper.findBestSchedules().stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }

    public int setScheduleLike(LikeToDTO likeDTO) {
        likeToMapper.setLike(likeDTO);
        return likeToMapper.getLikeCountByScheduleId(likeDTO.getRelatedId());
    }

    public int cancelScheduleLike(LikeToDTO likeDTO) {
        likeToMapper.cancelLike(likeDTO);
        return likeToMapper.getLikeCountByScheduleId(likeDTO.getRelatedId());
    }

    public void addSchedule(ScheduleDTO ScheduleDTO){
        scheduleMapper.addSchedule(ScheduleDTO);
    }

    public void updateSchedule(ScheduleDTO ScheduleDTO){
        scheduleMapper.updateSchedule(ScheduleDTO);
    }

    public int isExistSchedule(String id){
        return scheduleMapper.isExistSchedule(id);
    }

    public void insertScheSpot(List<ScheSpotDTO> scheSpot){
        for(int i =0; i<scheSpot.size();i++){
            scheSpotMapper.insertScheSpot(scheSpot.get(i));
            System.out.println("실시");
        }
        System.out.println("종료");
    }

    public void deleteScheSpot(String id){
        scheSpotMapper.deleteScheSpot(id);
    }

    public void insertMoney(List<MoneyDTO> money){
        for(int i =0; i<money.size();i++){
            moneyMapper.insertMoney(money.get(i));
            System.out.println("돈 실시");
        }
        System.out.println("돈 종료");
    }

    public void deleteMoney(String id){
        moneyMapper.deleteMoney(id);
    }


}
