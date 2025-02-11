package dev.jobposting.playground.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.jobposting.playground.domain.PaperSize;
import dev.jobposting.playground.service.JobPostingInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.jobposting.playground.service.JobPostingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobPostingApiController {
	private final JobPostingService jobPostingService;
	private final JobPostingInfoService jobPostingInfoService;

	// TODO: 정렬 문제
	// TODO: 애니메이션 추가

	@GetMapping
	public ResponseEntity<List<JobCardResponse>> getTopViewedJobs() {
		List<JobPostingResponse> jobPostingsResponses = jobPostingService.findTopViewedJobs();
		List<JobCardResponse> jobCardsResponse = new ArrayList<>();

		for (JobPostingResponse jobPostingResponse : jobPostingsResponses) {
			PaperSize paperSize = PaperSize.getSizeByViews(jobPostingResponse.getTotalViewCount());
			jobCardsResponse.add(JobCardResponse.from(jobPostingResponse, paperSize));
		}

		return ResponseEntity.ok(jobCardsResponse);
	}

	@PostMapping("/{id}/view")
	public ResponseEntity<Map<String, Integer>> increaseViewCount(@PathVariable("id") Long id) {
		int updatedCount = jobPostingInfoService.increaseViewCount(id);
		Map<String, Integer> response = new HashMap<>();
		response.put("totalViewCount", updatedCount);
		return ResponseEntity.ok(response);
	}
}
