package com.kvp.thinkjeonju.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kvp.thinkjeonju.dto.LikeToDTO;
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
	
	@GetMapping("/search")
	public String getSpotData(@RequestParam String dataValue, Model model, HttpSession session) {
	
		log.debug("[Search] Spot Data 검색");
		
		// 키워드로 api 통해 spot 검색 후 db에 저장
		ArrayList<SpotDTO> spots = spotService.getSpotData(dataValue);
		MemberDTO m = (MemberDTO)session.getAttribute("LOGIN_USER");
		
		if(m != null) {
			spots = spotService.setLikeInSpotDTOs(m, spots);
		}
		model.addAttribute("spots", spots);
		
		return "spotList";
	}
	
}