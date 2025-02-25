package dev.jobposting.playground.service;

import dev.jobposting.playground.domain.JobPosting;
import dev.jobposting.playground.domain.PaperSize;
import jakarta.annotation.PostConstruct;
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
    private final CurrentViewStorage currentViewStorage;

    @PostConstruct
    public void init() {
        for (long i = 1; i <= 6; i++) {
            clickCounts.put(i, 0);
            currentViewStorage.increase(String.valueOf(i));
        }
    }

    public List<JobPosting> getAllJobPostings() {
        List<String> sortedJobIds = getSortedJobIds();
        return createSortedJobPostings(sortedJobIds);
    }

    /**
     * 클릭 데이터를 기반으로 정렬된 jobId 리스트 반환
     */
    private List<String> getSortedJobIds() {
        List<String> sortedJobIds = currentViewStorage.getTopCurrentViewedJobs();

        return clickCounts.keySet().stream()
                .sorted((id1, id2) -> compareJobIds(id1, id2, sortedJobIds))
                .map(String::valueOf)
                .toList();
    }

    private int compareJobIds(Long id1, Long id2, List<String> sortedJobIds) {
        // 클릭 횟수가 많은 순서로 정렬
        int clickCount1 = clickCounts.getOrDefault(id1, 0);
        int clickCount2 = clickCounts.getOrDefault(id2, 0);
        if (clickCount1 != clickCount2) {
            return Integer.compare(clickCount2, clickCount1);
        }

        // 클릭 횟수가 같다면, FIFO 순서 유지
        int index1 = sortedJobIds.indexOf(String.valueOf(id1));
        int index2 = sortedJobIds.indexOf(String.valueOf(id2));

        // 리스트에 없는 경우 가장 뒤로 정렬
        if (index1 == -1) index1 = Integer.MAX_VALUE;
        if (index2 == -1) index2 = Integer.MAX_VALUE;

        return Integer.compare(index1, index2);
    }

    private List<JobPosting> createSortedJobPostings(List<String> sortedJobIds) {
        return sortedJobIds.stream()
                .map(jobId -> createJobPosting(Long.parseLong(jobId), currentViewStorage.getClickCount(jobId)))
                .collect(Collectors.toList());
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
