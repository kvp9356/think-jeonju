package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("")
    public ResponseEntity<Void> addMember(@RequestBody MemberDTO memberDTO, HttpSession session) {
        log.debug("[회원가입] 회원가입 요청");
        session.setAttribute("LOGIN_USER", memberService.addMember(memberDTO));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> checkIdDuplicate(@PathVariable String id) {
        log.debug("[회원가입] 아이디 중복 체크");
        memberService.checkIdDuplicate(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
