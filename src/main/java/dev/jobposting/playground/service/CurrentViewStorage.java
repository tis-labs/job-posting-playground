package dev.jobposting.playground.service;

import dev.jobposting.playground.event.query.ViewCount;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrentViewStorage {

    private static final int MAX_SIZE = 5;
    private final Deque<String> currentToPacket = new ArrayDeque<>();

    @PostConstruct
    public void init() {
        for (long i = 1; i <= 6; i++) {
            increase(String.valueOf(i));
        }
    }

    public synchronized void increase(String jobId) {
        currentToPacket.addLast(jobId);
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

    public List<ViewCount> getTopViewedJobsByLimit(int limit) {
        return currentToPacket.stream()
            .collect(Collectors.groupingBy(jobId -> jobId, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(limit)
            .map(entry -> new ViewCount(entry.getKey(), entry.getValue().intValue()))
            .toList();
    }
}
