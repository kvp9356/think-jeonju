package com.kvp.thinkjeonju.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.model.SpotImg;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/spots")
public class SpotController {

	@Autowired
	private SpotService spotService;
	
	final static int PAGESIZE = 8;
	
	@PostMapping("/{category}/search")
	public String getSpotData(@RequestParam(required=false, defaultValue="") String dataValue, Model model, HttpSession session, 
							  @RequestParam(required=false, defaultValue="1") int cPage,
							  @PathVariable String category) {
		log.debug("[Spot] 통합 검색");
		
		int ctgNum = SpotDTO.Category.valueOf(category).getCategoryNum();
		ArrayList<SpotDTO> spots = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		
		switch(ctgNum) {
		case 0: 
		case 1:
		case 2:
		case 3:
		case 4:
			map.put("startNum", ""+((cPage-1)*PAGESIZE+1));
			map.put("dataValue", dataValue);
			map.put("endNum", ""+cPage*PAGESIZE);
			map.put("category", ""+ctgNum);
			spots = spotService.getSpotData(map);
			break;
		default:
			map.put("dataValue", dataValue);
			map.put("startNum", ""+((cPage-1)*PAGESIZE+1));
			map.put("endNum", ""+cPage*PAGESIZE);
			spots = spotService.getSpotData(map);
			break;
		}

		
		int size = spotService.getSpotDataSize(map);
		
		MemberDTO m = (MemberDTO)session.getAttribute("loginUser");
		
		ArrayList<String> img = new ArrayList<>();
		
		for(int i=0; i<spots.size(); i++) {
			img = spotService.getSpotImg(spots.get(i).getId());
			spots.get(i).setImgUrl(img);
			spots.get(i).setLikeCnt(spotService.getLikeCnt(spots.get(i).getId()));
		}
		
		if(m != null) {
			spots = spotService.setLikeInSpotDTOs(m, spots);
		}
		
		model.addAttribute("spots", spots);
		model.addAttribute("dataValue", dataValue);
		model.addAttribute("category", category);
		model.addAttribute("cPage", cPage);
		model.addAttribute("size", size%PAGESIZE==0?size/PAGESIZE:size/PAGESIZE+1);
		
		return "spotList";
	}
	
	@PostMapping("/detail")
	public String getSpotDetail(@RequestParam String id, Model model, HttpSession session) {
		log.debug("[Spot] 상세보기 페이지 이동");
		
		SpotDTO spot = spotService.getSpotDetail(id).toDTO();
		spot.setImgUrl(spotService.getSpotImg(id));
		spot.setLikeCnt(spotService.getLikeCnt(id));
		
		MemberDTO m = (MemberDTO)session.getAttribute("loginUser");
		
		if(m != null) {
			if(spotService.getIsLike(new LikeToDTO(m.getId(), id, 's')) == 1) {
				spot.setIsLike(true);
			} else {
				spot.setIsLike(false);
			} 
		} 
		model.addAttribute("spot", spot);
		
		return "spotDetail";
	}
}