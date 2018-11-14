package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.util.List;

@Alias("schedule")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private String id;
    private String title;
    private String memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int isPublic;
    private String thumnailUrl;
    private int isWriting;
    private List<ScheSpotDTO> ScheSpot;
    private List<MoneyDTO> Money;

    public static Schedule from(ScheduleDTO scheduleDTO) {
        return new Schedule(scheduleDTO.getId(), scheduleDTO.getTitle(), scheduleDTO.getMemberId(),
                scheduleDTO.getStartDate(), scheduleDTO.getEndDate(), scheduleDTO.getIsPublic(),
                scheduleDTO.getThumnailUrl(), scheduleDTO.getIsWriting() ,scheduleDTO.getScheSpot(), scheduleDTO.getMoney());
    }

    public ScheduleDTO toDTO() {
        return new ScheduleDTO(id, title, memberId, startDate, endDate, isPublic, thumnailUrl,  isWriting,0,false,null,null);
    }
}
