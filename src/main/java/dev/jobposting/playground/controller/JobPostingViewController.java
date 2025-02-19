package dev.jobposting.playground.controller;

import dev.jobposting.playground.geo.GeoLocation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobPostingViewController {

	@GetMapping
	public String showJobPostings(GeoLocation geoLocation, Model model) {
		model.addAttribute("geoLocation", geoLocation);
		return "jobPostings";
	}
}
