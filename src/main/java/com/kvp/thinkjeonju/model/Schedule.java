package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.ScheduleDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Alias("schedule")
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private String url;
    private String title;
    private String memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int isPublic;

    public static Schedule from(ScheduleDTO scheduleDTO) {
        return new Schedule(scheduleDTO.getUrl(), scheduleDTO.getTitle(), scheduleDTO.getMemberId(),
                scheduleDTO.getStartDate(), scheduleDTO.getEndDate(), scheduleDTO.getIsPublic());
    }

    public ScheduleDTO toDTO() {
        return new ScheduleDTO(url, title, memberId, startDate, endDate, isPublic);
    }
}
