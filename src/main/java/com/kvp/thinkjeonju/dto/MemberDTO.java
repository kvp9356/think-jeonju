package com.kvp.thinkjeonju.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String id;
    private String password;
    private String name;

    public MemberDTO erasePassword() {
        this.password = "";
        return this;
    }
}