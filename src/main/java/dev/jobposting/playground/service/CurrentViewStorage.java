package dev.jobposting.playground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrentViewStorage {

    private static final int MAX_SIZE = 5; // 최대 저장 개수
    private final Deque<String> currentToPacket = new ArrayDeque<>();

    public synchronized void increase(String jobId) {
        currentToPacket.addLast(jobId);
    }

    private void removeOldestIfExceedsLimit() {
        if (currentToPacket.size() > MAX_SIZE) {
            currentToPacket.pollFirst();
        }
    }

    /**
     * 최근 클릭 데이터를 기준으로 정렬된 Job ID 리스트 반환
     */
    public List<String> getTopCurrentViewedJobs() {
        // 클릭 횟수 집계 (각 jobId별 등장 횟수 저장)
        Map<String, Long> jobClickCounts = getClickFrequencyMap();

        // 클릭된 순서를 유지하기 위해 리스트 변환
        List<String> recentClickedJobs = new ArrayList<>(currentToPacket);

        // 정렬된 리스트 반환
        return jobClickCounts.entrySet().stream()
                .sorted((job1, job2) -> compareJobsByClickCountAndOrder(job1, job2, recentClickedJobs))
                .map(Map.Entry::getKey)
                .toList();
    }

    private int compareJobsByClickCountAndOrder(Map.Entry<String, Long> job1, Map.Entry<String, Long> job2,
                                                List<String> recentClickedJobs) {
        String jobId1 = job1.getKey();
        String jobId2 = job2.getKey();
        long clickCount1 = job1.getValue();
        long clickCount2 = job2.getValue();

        // 클릭 횟수가 많은 순서로 정렬
        int compareByClickCount = Long.compare(clickCount2, clickCount1);
        if (compareByClickCount != 0) {
            return compareByClickCount;
        }

        // 클릭 횟수가 같다면, FIFO 순서 유지
        int index1 = recentClickedJobs.indexOf(jobId1);
        int index2 = recentClickedJobs.indexOf(jobId2);

        return Integer.compare(index1, index2);
    }

    private Map<String, Long> getClickFrequencyMap() {
        return currentToPacket.stream()
                .collect(Collectors.groupingBy(jobId -> jobId, LinkedHashMap::new, Collectors.counting()));
    }

    public int getClickCount(String jobId) {
        return (int) currentToPacket.stream().filter(id -> id.equals(jobId)).count();
    }
}
