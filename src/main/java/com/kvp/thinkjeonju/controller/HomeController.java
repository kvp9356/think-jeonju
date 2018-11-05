package com.kvp.thinkjeonju.controller;

import com.kvp.thinkjeonju.service.ScheduleService;
import com.kvp.thinkjeonju.service.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private SpotService spotService;

	@Autowired
	private ScheduleService scheduleService;
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("bestSpots", spotService.getBestSpots());
		model.addAttribute("bestSchedules", scheduleService.getBestSchedules());
		return "index";
	}
}
