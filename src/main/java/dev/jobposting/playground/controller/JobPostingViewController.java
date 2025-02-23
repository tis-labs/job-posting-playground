package dev.jobposting.playground.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobPostingViewController {

	@GetMapping
	public String showJobPostings() {
		return "jobPostings";
	}
}
