package dev.jobposting.playground.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jobposting.playground.service.JobPostingService;

import dev.jobposting.playground.ui2.PaperSize;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobPostingApiController {
	private final JobPostingService jobPostingService;

	@GetMapping
	public ResponseEntity<List<JobCardResponse>> getTopViewedJobs() {
		List<JobPostingResponse> jobPostingsResponses = jobPostingService.findTopViewedJobs();
		List<JobCardResponse> jobCardsResponse = new ArrayList<>();
		for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
			jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, PaperSize.random()));
		}
		return ResponseEntity.ok(jobCardsResponse);
	}
}
