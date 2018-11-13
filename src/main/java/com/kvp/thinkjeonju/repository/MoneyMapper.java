package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.model.ScheSpot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MoneyMapper {
    void insertMoney(MoneyDTO money);

    List<ScheSpot> getScheSpot(String id);

    void deleteMoney(String Id);
}
