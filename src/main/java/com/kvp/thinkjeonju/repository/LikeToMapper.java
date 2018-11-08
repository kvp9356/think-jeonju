package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.PagingDTO;
import com.kvp.thinkjeonju.model.Schedule;
import com.kvp.thinkjeonju.model.Spot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeToMapper {

    int getLikeSpotsCount(String memberId);

    List<Spot> findSpotsByMemberIdAndPage(PagingDTO paging);

    int getLikeSchedulesCount(String memberId);

    List<Schedule> findSchedulesByMemberId(PagingDTO paging);
}
