package dev.jobposting.playground.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.service.JobPostingService;
import dev.jobposting.playground.ui.Board;
import dev.jobposting.playground.ui.JobCard;
import dev.jobposting.playground.ui.CardMaker;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobPostingController {
	private final JobPostingService jobPostingService;

	@GetMapping
	public String showJobPostings() {
		return "jobPostings";
	}

	@PostMapping("/updateSize")
	@ResponseBody
	public List<JobCard> updateBoardSize(@RequestParam int width, @RequestParam int height) {
		return placeCards(width, height);
	}

	private List<JobCard> placeCards(int width, int height) {
		List<JobPosting> postings = jobPostingService.findTopViewedJobs();
		List<JobCard> jobCards = CardMaker.makeCard(postings);
		Board board = new Board(width, height);
		board.placeCards(jobCards);
		return jobCards;
	}
}
