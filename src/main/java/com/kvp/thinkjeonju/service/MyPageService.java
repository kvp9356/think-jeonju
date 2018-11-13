package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.*;
import com.kvp.thinkjeonju.repository.LikeToMapper;
import com.kvp.thinkjeonju.repository.ScheduleMapper;
import com.kvp.thinkjeonju.repository.SpotMapper;
import com.kvp.thinkjeonju.support.Paging;
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

    public PageDTO<SpotDTO> getLikeSpotsByPageNo(MemberDTO user, int pageNo) {
        Paging paging = new Paging();
        paging.makeLastPage(likeToMapper.getLikeSpotsCountByMemberId(user.getId()));
        paging.makeBlock(pageNo);
        paging.makeCurrentBlock(pageNo);
        return new PageDTO<>(getLikeSpots(user, paging), paging);
    }

    private List<SpotDTO> getLikeSpots(MemberDTO user, Paging paging) {
        return likeToMapper.findSpotsByMemberIdAndPage(new PagingDTO(user.getId(), paging)).stream()
                .map(spot -> spot.toDTO())
                .map(spotDTO -> {
                    spotDTO.setImgUrl(spotMapper.findSpotImgUrlById(spotDTO.getId()));
                    spotDTO.setLikeCnt(spotMapper.getLikeCnt(spotDTO.getId()));
                    return spotDTO;
                })
                .collect(Collectors.toList());
    }

    public PageDTO<ScheduleDTO> getLikeSchedulesByPageNo(MemberDTO user, int pageNo) {
        Paging paging = new Paging();
        paging.makeLastPage(likeToMapper.getLikeSchedulesCountByMemberId(user.getId()));
        paging.makeBlock(pageNo);
        paging.makeCurrentBlock(pageNo);
        return new PageDTO<>(getLikeSchedules(user, paging), paging);
    }

    private List<ScheduleDTO> getLikeSchedules(MemberDTO user, Paging paging) {
        return likeToMapper.findSchedulesByMemberId(new PagingDTO(user.getId(), paging)).stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }

    public PageDTO<ScheduleDTO> getPersonalSchedulesByPageNo(MemberDTO user, int pageNo) {
        Paging paging = new Paging();
        paging.makeLastPage(scheduleMapper.getPersonalSchedulesCount(user.getId()));
        paging.makeBlock(pageNo);
        paging.makeCurrentBlock(pageNo);
        return new PageDTO<>(getPersonalSchedules(user, paging), paging);
    }

    private List<ScheduleDTO> getPersonalSchedules(MemberDTO user, Paging paging) {
        return scheduleMapper.findByMemberId(new PagingDTO(user.getId(), paging)).stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }
}
