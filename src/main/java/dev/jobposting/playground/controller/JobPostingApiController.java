package dev.jobposting.playground.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jobposting.playground.service.JobPostingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobPostingApiController {
	private final JobPostingService jobPostingService;

	@GetMapping
	public ResponseEntity<List<JobPostingResponse>> getTopViewedJobs() {
		List<JobPostingResponse> response = jobPostingService.findTopViewedJobs();
		return ResponseEntity.ok(response);
	}
}
