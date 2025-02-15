package dev.jobposting.playground.service;

import dev.jobposting.playground.controller.JobPostingResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class MockJobPostingService implements JobPostingService {

	private final JobPostingInfoService jobPostingInfoService;

	public MockJobPostingService(JobPostingInfoService jobPostingInfoService) {
		this.jobPostingInfoService = jobPostingInfoService;
	}

	@Override
	public List<JobPostingResponse> findTopViewedJobs() {
		return jobPostingInfoService.getAllJobPostings().stream()
				.map(job -> new JobPostingResponse(
						job.getId(),
						job.getFiveMinViewCount(),
						job.getTotalViewCount(),
						"샘플 공고 " + job.getId(),
						"샘플 설명",
						"샘플 회사",
						"default-url"
				))
				.sorted((x, y) -> Integer.compare(y.getFiveMinViewCount(), x.getFiveMinViewCount()))
				.toList();
	}
}
