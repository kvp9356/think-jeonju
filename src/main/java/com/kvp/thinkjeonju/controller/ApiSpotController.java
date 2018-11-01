package com.kvp.thinkjeonju.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.exception.common.DataBaseException;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/spots")
public class ApiSpotController {

	@Autowired
	private SpotService spotService;
	
	@PostMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> setSpotLike(@PathVariable String spotId, HttpSession session) {
		MemberDTO m = (MemberDTO)session.getAttribute("LOGIN_USER");
		
		LikeToDTO like = new LikeToDTO(m.getId(), spotId, 's');
		spotService.setSpotLike(like);
		int likeCnt = spotService.getLikeCnt(spotId);
		return new ResponseEntity(likeCnt, HttpStatus.OK);		
	}
	
	@DeleteMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> cancelSpotLike(@PathVariable String spotId, HttpSession session) {
		MemberDTO m = (MemberDTO)session.getAttribute("LOGIN_USER");
		
		LikeToDTO like = new LikeToDTO(m.getId(), spotId, 's');
		spotService.cancelSpotLike(like);
		int likeCnt = spotService.getLikeCnt(spotId);
		return new ResponseEntity(likeCnt, HttpStatus.OK);
	}
}
