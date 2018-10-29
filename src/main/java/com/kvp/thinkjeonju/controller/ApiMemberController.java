package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/members")
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("")
    public ResponseEntity<Void> addMember(@RequestBody @Valid MemberDTO memberDTO, HttpSession session) {
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

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberDTO memberDTO, HttpSession session) {
        log.debug("[로그인] 로그인 요청");
        session.setAttribute("LOGIN_USER", memberService.login(memberDTO));
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        log.debug("[로그아웃] 로그아웃 요청");
        session.removeAttribute("LOGIN_USER");
        return new ResponseEntity(HttpStatus.OK);
    }
}
