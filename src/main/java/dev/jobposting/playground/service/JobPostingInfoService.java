package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.domain.PaperSize;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobPostingInfoService {

    private final CurrentViewStorage currentViewStorage;
    private final TotalViewStorage totalViewStorage;

    public List<JobPosting> getAllJobPostings() {
        return totalViewStorage.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> e) -> e.getValue()).reversed())
                .map(e -> createJobPosting(e.getKey(), currentViewStorage.getClickCount(String.valueOf(e.getKey()))))
                .toList();
    }

    private JobPosting createJobPosting(String jobId, int totalClickCount) {
        PaperSize paperSize = PaperSize.getSizeByViews(totalClickCount);
        return JobPosting.builder()
                .id(jobId)
                .currentViewCount(totalClickCount)
                .totalViewCount(totalViewStorage.getClickCount(String.valueOf(jobId)))
                .width(paperSize.getWidth())
                .height(paperSize.getHeight())
                .build();
    }

    public int increaseViewCount(Long jobId) {
        int updatedCount = totalViewStorage.increase(String.valueOf(jobId));
        currentViewStorage.increase(String.valueOf(jobId));
        return updatedCount;
    }

    public void resetClickCount() {
        totalViewStorage.clear();
    }
}
