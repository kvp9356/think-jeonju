package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.PagingDTO;
import com.kvp.thinkjeonju.model.Schedule;
import com.kvp.thinkjeonju.model.Spot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeToMapper {

    int getLikeSpotsCountByMemberId(String memberId);

    List<Spot> findSpotsByMemberIdAndPage(PagingDTO paging);

    int getLikeSchedulesCountByMemberId(String memberId);

    List<Schedule> findSchedulesByMemberId(PagingDTO paging);

    int setLike(LikeToDTO likeDTO);

    int cancelLike(LikeToDTO likeDTO);

    int getLikeCountByScheduleId(String relatedId);

    int getLikeCountBySpotId(String relatedId);
}
