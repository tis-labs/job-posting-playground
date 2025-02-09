package dev.jobposting.playground.controller;

import java.util.ArrayList;
import java.util.List;

import dev.jobposting.playground.domain.PaperSize;
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

	// TODO: 1. 조회수 증가 로직 추가
	// TODO: 2. 1분 뒤 크기 변경

	@GetMapping
	public ResponseEntity<List<JobCardResponse>> getTopViewedJobs() {
		List<JobPostingResponse> jobPostingsResponses = jobPostingService.findTopViewedJobs();
		List<JobCardResponse> jobCardsResponse = new ArrayList<>();
		for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
			jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, PaperSize.getSizeByViews(jobPostingResponse.getTotalViewCount())));
		}
		return ResponseEntity.ok(jobCardsResponse);
	}
}
