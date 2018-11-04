package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.repository.LikeToMapper;
import com.kvp.thinkjeonju.repository.ScheduleMapper;
import com.kvp.thinkjeonju.repository.SpotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private LikeToMapper likeToMapper;

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    public List<SpotDTO> getLikeSpots(MemberDTO user) {
        return likeToMapper.findSpotsByMemberId(user.getId()).stream()
                .map(spot -> spot.toDTO())
                .map(spotDTO -> {
                    spotDTO.setImgUrl(spotMapper.findSpotImgUrlById(spotDTO.getId()));
                    spotDTO.setLikeCnt(spotMapper.getLikeCnt(spotDTO.getId()));
                    return spotDTO;
                })
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getLikeSchedules(MemberDTO user) {
        return likeToMapper.findSchedulesByMemberId(user.getId()).stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getPersonalSchedules(MemberDTO user) {
        return scheduleMapper.findByMemberId(user.getId()).stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }
}
