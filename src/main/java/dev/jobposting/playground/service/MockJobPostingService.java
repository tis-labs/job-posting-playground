package dev.jobposting.playground.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import dev.jobposting.playground.controller.JobPostingResponse;

@Primary
@Service
public class MockJobPostingService implements JobPostingService {

	@Override
	public List<JobPostingResponse> findTopViewedJobs() {
		List<JobPostingResponse> postings = Arrays.asList(
			new JobPostingResponse(1L, 3, 5,"ABC 회사 백엔드 채용공고", "경력 3년차", "ABC", "default-url"),
			new JobPostingResponse(2L, 2, 3,"XYZ 회사 백엔드 채용공고", "경력 1년차", "XYZ", "default-url"),
			new JobPostingResponse(3L, 1, 2,"DEF 회사 백엔드 채용공고", "경력 1년차", "DEF", "default-url"),
			new JobPostingResponse(4L, 1, 2,"TT 회사 백엔드 채용공고", "경력 1년차", "TT", "default-url"),
			new JobPostingResponse(5L, 1, 2,"WW 회사 백엔드 채용공고", "경력 1년차", "WW", "default-url"),
			new JobPostingResponse(6L, 1, 2,"KK 회사 백엔드 채용공고", "경력 1년차", "KK", "default-url")
		);
		postings.sort((x,y) -> Integer.compare(y.getFiveMinViewCount(), x.getFiveMinViewCount()));
		return postings;
	}
}
