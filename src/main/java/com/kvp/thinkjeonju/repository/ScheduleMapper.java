package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.model.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<Schedule> findByMemberId(String memberId);

    int getLikeCnt(String id);
}
