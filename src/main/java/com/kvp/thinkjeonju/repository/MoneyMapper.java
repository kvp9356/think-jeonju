package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.model.Money;
import com.kvp.thinkjeonju.model.ScheSpot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MoneyMapper {
    void insertMoney(MoneyDTO money);

    List<Money> getMoneyById(String scheduleId);

    void deleteMoney(String Id);

    List<Money> getSpotMoney(MoneyDTO money);
}
