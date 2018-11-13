package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
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

    public static ScheSpot from(ScheSpotDTO ScheSpotDTO) {
        return new ScheSpot(ScheSpotDTO.getId(),ScheSpotDTO.getScheDate(),ScheSpotDTO.getSequence(),ScheSpotDTO.getSpotId());
    }

    public ScheSpotDTO toDTO() {
        return new ScheSpotDTO(id,scheDate,sequence,spotId);
    }
}
