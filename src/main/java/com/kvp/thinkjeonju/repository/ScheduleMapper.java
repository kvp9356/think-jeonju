package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.PagingDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.model.Schedule;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    int getPersonalSchedulesCount(String memberId);

    List<Schedule> findByMemberId(PagingDTO paging);

    int getLikeCnt(String id);

    List<Schedule> findBestSchedules();

    void addSchedule(ScheduleDTO shedule);
}
