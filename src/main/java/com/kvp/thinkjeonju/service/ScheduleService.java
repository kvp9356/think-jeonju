package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.model.Schedule;
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
}
