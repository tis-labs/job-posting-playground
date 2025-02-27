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

    public List<String> getTopCurrentViewedJobs() {
        return currentToPacket.stream()
                .collect(Collectors.groupingBy(jobId -> jobId, Collectors.summingInt(jobId -> 1)))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    public int getClickCount(String jobId) {
        return (int) currentToPacket.stream().filter(id -> id.equals(jobId)).count();
    }
}
