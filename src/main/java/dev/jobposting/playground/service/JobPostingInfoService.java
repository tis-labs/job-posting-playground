package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.domain.PaperSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingInfoService {

    private static final Map<Long, Integer> clickCounts = new HashMap<>();

    /**
     * 데이터 생성 (초기 조회수 0)
     */
    static {
        for (long i = 1; i <= 6; i++) {
            clickCounts.put(i, 0);
        }
    }

    public List<JobPosting> getAllJobPostings() {
        return clickCounts.entrySet().stream()
                .map(entry -> createJobPosting(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private JobPosting createJobPosting(Long jobId, int clickCount) {
        PaperSize paperSize = calculateSize(clickCount);
        return JobPosting.builder()
                .id(jobId)
                .fiveMinViewCount(clickCount)
                .totalViewCount(clickCount)
                .width(paperSize.getWidth())
                .height(paperSize.getHeight())
                .build();
    }

    private PaperSize calculateSize(int clickCount) {
        return PaperSize.getSizeByViews(clickCount);
    }

    public int increaseViewCount(Long jobId) {
        int updatedCount = clickCounts.getOrDefault(jobId, 0) + 1;
        clickCounts.put(jobId, updatedCount);
        return updatedCount;
    }
}
