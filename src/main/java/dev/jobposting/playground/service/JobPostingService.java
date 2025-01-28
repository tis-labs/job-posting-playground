package dev.jobposting.playground.service;

import java.util.List;

import dev.jobposting.playground.domain.JobPosting;

public interface JobPostingService {
	List<JobPosting> findTopViewedJobs();
}
