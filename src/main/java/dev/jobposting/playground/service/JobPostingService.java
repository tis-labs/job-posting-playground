package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingService {
    private static final Map<Long, Integer> clickCounts = new HashMap<>(); // 조회수 저장소

    static {
        for (long i = 1; i <= 6; i++) {
            clickCounts.put(i, 0); // 처음에는 조회수 0으로 세팅
        }
    }

    public List<JobPosting> getAllJobPostings() {
        return clickCounts.entrySet().stream()
                .map(entry -> JobPosting.builder()
                        .id(entry.getKey())
                        .clickCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public void increaseClickCount(Long jobId) {
        clickCounts.put(jobId, clickCounts.getOrDefault(jobId, 0) + 1);
    }
}
