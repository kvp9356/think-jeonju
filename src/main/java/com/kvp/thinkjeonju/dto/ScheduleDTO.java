package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleDTO {
    private String url;
    private String title;
    private String memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int isPublic;
}
