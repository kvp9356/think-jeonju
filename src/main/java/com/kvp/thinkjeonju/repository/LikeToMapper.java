package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.model.Schedule;
import com.kvp.thinkjeonju.model.Spot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeToMapper {
    List<Spot> findSpotsByMemberId(String memberId);

    List<Schedule> findSchedulesByMemberId(String memberId);
}
