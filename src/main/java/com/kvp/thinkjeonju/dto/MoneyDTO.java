package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoneyDTO {
    private String id;
    private String scheSpotId;
    private String name;
    private int amount;
    private LocalDate scheDate;

}
