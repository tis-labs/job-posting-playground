package dev.jobposting.playground.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobPostingViewController {

	@GetMapping
	public String showJobPostings() {
		return "jobPostings";
	}
}
