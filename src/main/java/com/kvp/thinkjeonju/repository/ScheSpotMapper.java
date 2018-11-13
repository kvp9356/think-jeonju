package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.model.ScheSpot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheSpotMapper {
    void insertScheSpot(ScheSpotDTO schespot);

    List<ScheSpot> getScheSpot(String id);

    void deleteScheSpot(String Id);
}
