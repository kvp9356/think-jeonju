package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScheduleDTO {
    private String id;
    private String title;
    private String memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int isPublic;
    private String thumnailUrl;
    private int isWriting;
    private int like;
    private Boolean isLike;
    private List<ScheSpotDTO> ScheSpot;
    private List<MoneyDTO> Money;


    public void setLike(int like) {
        this.like = like;
    }

    public void setIsLike(int isExists) {
        this.isLike = (isExists == 1);
    }
}
