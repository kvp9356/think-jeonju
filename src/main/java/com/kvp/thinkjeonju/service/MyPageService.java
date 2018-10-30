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
        List<SpotDTO> likeSpots = likeToMapper.findSpotsByMemberId(user.getId()).stream()
                .map(spot -> spot.toDTO())
                .collect(Collectors.toList());
        likeSpots.stream()
                .forEach(spotDTO -> spotDTO.setImgUrl(spotMapper.findSpotImgUrlById(spotDTO.getId())));
        return likeSpots;
    }

    public List<ScheduleDTO> getLikeSchedules(MemberDTO user) {
        return likeToMapper.findSchedulesByMemberId(user.getId()).stream()
                .map(schedule -> schedule.toDTO())
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getPersonalSchedules(MemberDTO user) {
        return scheduleMapper.findByMemberId(user.getId()).stream()
                .map(schedule -> schedule.toDTO())
                .collect(Collectors.toList());
    }
}
