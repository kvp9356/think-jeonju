package com.kvp.thinkjeonju.dto;

import com.kvp.thinkjeonju.support.Paging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> results;
    private Paging paging;
}
