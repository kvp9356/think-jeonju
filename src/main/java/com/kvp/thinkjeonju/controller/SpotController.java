package com.kvp.thinkjeonju.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/spots")
public class SpotController {

	@Autowired
	private SpotService spotService;
	
	@PostMapping("/search")
	public ResponseEntity<ArrayList<SpotDTO>> getSpotData(@RequestParam String dataValue) {
	
		log.debug("[Search] Spot Data 검색");
		
		// 키워드로 api 통해 spot 검색 후 db에 저장
		ArrayList<SpotDTO> spots = spotService.getSpotData(dataValue);

		return new ResponseEntity<>(spots, HttpStatus.OK);
	}
	
}
