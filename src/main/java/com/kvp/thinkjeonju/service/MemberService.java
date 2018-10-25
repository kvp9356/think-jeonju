package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.exception.common.DataBaseException;
import com.kvp.thinkjeonju.exception.member.IdAlreadyExistsException;
import com.kvp.thinkjeonju.exception.member.PasswordNotMatchException;
import com.kvp.thinkjeonju.exception.member.UserNotFoundException;
import com.kvp.thinkjeonju.model.Member;
import com.kvp.thinkjeonju.repository.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberDTO addMember(MemberDTO memberDTO) {
        checkIdDuplicate(memberDTO.getId());

        Member member = Member.from(memberDTO).encryptPassword(bCryptPasswordEncoder);
        int result = memberMapper.addMember(member);
        if(result <= 0)
            throw new DataBaseException("회원가입에 실패했습니다.");
        return member.toDTO().erasePassword();
    }

    public void checkIdDuplicate(String id) {
        if(memberMapper.existsById(id) != 0)
            throw new IdAlreadyExistsException("이미 존재하는 ID입니다.");
    }

    public MemberDTO login(MemberDTO memberDTO) {
        Member member = memberMapper.findById(memberDTO.getId());
        if(member == null)
            throw new UserNotFoundException("회원을 찾을 수 없습니다.");

        if(!member.isEqualPassword(bCryptPasswordEncoder, memberDTO.getPassword()))
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        return member.toDTO().erasePassword();
    }
}
