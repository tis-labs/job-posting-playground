package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.domain.PaperSize;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JobPostingInfoService {

    private static final Map<Long, Integer> clickCounts = new ConcurrentHashMap<>();
    private final CurrentViewStorage currentViewStorage;

    @PostConstruct
    public void init() {
        for (long i = 1; i <= 6; i++) {
            clickCounts.put(i, 0);
            currentViewStorage.increase(String.valueOf(i));
        }
    }

    public List<JobPosting> getAllJobPostings() {
        return clickCounts.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<Long, Integer> e) -> e.getValue()).reversed())
                .map(e -> createJobPosting(e.getKey(), currentViewStorage.getClickCount(String.valueOf(e.getKey()))))
                .toList();
    }

    private JobPosting createJobPosting(Long jobId, int clickCount) {
        PaperSize paperSize = calculateSize(clickCount);
        return JobPosting.builder()
                .id(jobId)
                .fiveMinViewCount(clickCount)
                .totalViewCount(clickCounts.getOrDefault(jobId, 0))
                .width(paperSize.getWidth())
                .height(paperSize.getHeight())
                .build();
    }

    public int increaseViewCount(Long jobId) {
        incrementClickCount(jobId);
        updateRecentClicks(jobId);
        return clickCounts.get(jobId);
    }

    private void incrementClickCount(Long jobId) {
        clickCounts.put(jobId, clickCounts.getOrDefault(jobId, 0) + 1);
    }

    private void updateRecentClicks(Long jobId) {
        currentViewStorage.increase(String.valueOf(jobId));
    }

    private PaperSize calculateSize(int clickCount) {
        return PaperSize.getSizeByViews(clickCount);
    }
}
