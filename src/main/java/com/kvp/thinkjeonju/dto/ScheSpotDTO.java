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
public class ScheSpotDTO {
    private String id;
    private LocalDate scheDate;
    private int sequence;
    private String spotId;
    private String spotimg;
    private int likeCnt;
    private List<MoneyDTO> Money;
    private SpotDTO spot;

}
