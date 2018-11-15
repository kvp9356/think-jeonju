package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.MoneyDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Alias("money")
@NoArgsConstructor
@AllArgsConstructor
public class Money {
    private String id;
    private String scheSpotId;
    private String name;
    private int amount;
    private LocalDate scheDate;

    public static Money from(MoneyDTO MoneyDTO) {
        return new Money(MoneyDTO.getId(),MoneyDTO.getScheSpotId(),MoneyDTO.getName(),MoneyDTO.getAmount(),MoneyDTO.getScheDate());
    }

    public MoneyDTO toDTO() {
        return new MoneyDTO(id,scheSpotId,name,amount,scheDate);
    }
}
