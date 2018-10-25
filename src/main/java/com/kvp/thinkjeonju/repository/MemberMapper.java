package com.kvp.thinkjeonju.repository;

import com.kvp.thinkjeonju.model.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    int addMember(Member member);
    int existsById(String id);
    Member findById(String id);
}
