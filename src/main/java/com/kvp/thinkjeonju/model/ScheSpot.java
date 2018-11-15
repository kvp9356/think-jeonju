package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.util.List;

@Alias("schespot")
@NoArgsConstructor
@AllArgsConstructor
public class ScheSpot {
    private String id;
    private LocalDate scheDate;
    private int sequence;
    private String spotId;
    private String spotimg;
    private int likeCnt;


    public static ScheSpot from(ScheSpotDTO scheSpotDTO) {
        return new ScheSpot(scheSpotDTO.getId(),scheSpotDTO.getScheDate(),scheSpotDTO.getSequence(),scheSpotDTO.getSpotId(),scheSpotDTO.getSpotimg(),scheSpotDTO.getLikeCnt());
    }

    public ScheSpotDTO toDTO() {
        return new ScheSpotDTO(id,scheDate,sequence,spotId,null,0,null,null);
    }
}
