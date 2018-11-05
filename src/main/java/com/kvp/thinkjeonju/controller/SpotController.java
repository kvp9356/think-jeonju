package com.kvp.thinkjeonju.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/spots")
public class SpotController {

	@Autowired
	private SpotService spotService;
	
	@PostMapping("/search")
	public String getSpotData(@RequestParam String dataValue, Model model, HttpSession session, 
							  @RequestParam(required=false, defaultValue="1") int cPage) {
		log.debug("[Search] Spot Data 검색");
			
		// 키워드로 api 통해 spot 검색 후 db에 저장
		ArrayList<SpotDTO> spots = spotService.getSpotData(dataValue, cPage);
		MemberDTO m = (MemberDTO)session.getAttribute("LOGIN_USER");
		int size = spotService.getSpotDataSize(dataValue);
		
		if(m != null) {
			spots = spotService.setLikeInSpotDTOs(m, spots);
		}
		model.addAttribute("spots", spots);
		model.addAttribute("dataValue", dataValue);
		model.addAttribute("size", size);
		
		return "spotList";
	}
	
}