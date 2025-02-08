package dev.jobposting.playground.service;

import java.util.List;

import dev.jobposting.playground.controller.JobPostingResponse;

public interface JobPostingService {
	List<JobPostingResponse> findTopViewedJobs();
}
