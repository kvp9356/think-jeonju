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
import com.kvp.thinkjeonju.service.SearchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/searches")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@PostMapping("")
	public ResponseEntity<ArrayList<SpotDTO>> getSpotData(@RequestParam String dataValue) {
	
		log.debug("[Search] Spot Data 검색");
		
		// 키워드로 api 통해 spot 검색 후 db에 저장
		searchService.getSpotData(dataValue);
		
		// db에서 해당 spot과 img 불러옴
		ArrayList<SpotDTO> spots = searchService.searchSpot(dataValue);
		/*for(int i=0; i<spots.size(); i++) {
			System.out.println(spots.get(i).getImgUrl());
		}*/
		return new ResponseEntity<>(spots, HttpStatus.OK);
	}
	
}
