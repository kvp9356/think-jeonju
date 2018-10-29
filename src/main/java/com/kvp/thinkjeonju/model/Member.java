package com.kvp.thinkjeonju.model;

import com.kvp.thinkjeonju.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Alias("member")
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String id;
    private String password;
    private String name;

    public static Member from(MemberDTO memberDTO) {
        return new Member(memberDTO.getId(), memberDTO.getPassword(), memberDTO.getName());
    }

    public Member encryptPassword(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.password = bCryptPasswordEncoder.encode(this.password);
        return this;
    }

    public MemberDTO toDTO() {
        return new MemberDTO(id, password, name);
    }

    public boolean isEqualPassword(BCryptPasswordEncoder bCryptPasswordEncoder, String password) {
        return bCryptPasswordEncoder.matches(password, this.password);
    }
}
