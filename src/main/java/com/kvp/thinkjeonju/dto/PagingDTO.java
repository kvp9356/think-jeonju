package com.kvp.thinkjeonju.dto;

import com.kvp.thinkjeonju.support.Paging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PagingDTO {
    private String memberId;
    private Paging paging;
}
