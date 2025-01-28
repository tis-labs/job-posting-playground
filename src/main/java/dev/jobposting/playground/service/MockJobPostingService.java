package dev.jobposting.playground.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import dev.jobposting.playground.domain.JobPosting;

@Primary
@Service
public class MockJobPostingService implements JobPostingService {

	@Override
	public List<JobPosting> findTopViewedJobs() {
		List<JobPosting> postings = Arrays.asList(
			new JobPosting(500, "ABC 회사 백엔드 채용공고", "경력 3년차", "ABC", "default-url"),
			new JobPosting(300, "XYZ 회사 백엔드 채용공고", "경력 1년차", "XYZ", "default-url"),
			new JobPosting(200, "DEF 회사 백엔드 채용공고", "경력 1년차", "DEF", "default-url"),
			new JobPosting(150, "TT 회사 백엔드 채용공고", "경력 1년차", "TT", "default-url"),
			new JobPosting(150, "WW 회사 백엔드 채용공고", "경력 1년차", "WW", "default-url"),
			new JobPosting(100, "KK 회사 백엔드 채용공고", "경력 1년차", "KK", "default-url")
		);
		return postings;
	}
}
