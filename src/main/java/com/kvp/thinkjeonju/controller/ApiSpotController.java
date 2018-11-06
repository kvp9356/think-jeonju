package com.kvp.thinkjeonju.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.model.Spot;
import com.kvp.thinkjeonju.security.LoginUser;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/spots")
public class ApiSpotController {

	@Autowired
	private SpotService spotService;
	
	@PostMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> setSpotLike(@PathVariable String spotId, @LoginUser MemberDTO user) {
		log.debug("[좋아요] {} 장소 좋아요 추가", spotId);
		spotService.setSpotLike(new LikeToDTO(user.getId(), spotId, 's'));
		return new ResponseEntity<>(spotService.getLikeCnt(spotId), HttpStatus.OK);		
	}
	
	@DeleteMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> cancelSpotLike(@PathVariable String spotId, @LoginUser MemberDTO user) {
		log.debug("[좋아요] {} 장소 좋아요 취소", spotId);
		spotService.cancelSpotLike(new LikeToDTO(user.getId(), spotId, 's'));
		return new ResponseEntity<>(spotService.getLikeCnt(spotId), HttpStatus.OK);
	}

}


