package dev.jobposting.playground.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TotalViewStorage {
	private final Map<String, Integer> clickCounts = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		for (long i = 1; i <= 6; i++) {
			increase(String.valueOf(i));
		}
	}
	
	public int increase(String jobId) {
		return clickCounts.compute(jobId, (id, count) -> count == null ? 1 : count + 1);
	}
	
	public int getClickCount(String jobId) {
		return clickCounts.getOrDefault(jobId, 0);
	}

	public void clear() {
		clickCounts.clear();
	}
	
	public Set<Map.Entry<String, Integer>>  entrySet() {
		return clickCounts.entrySet();
	}

	public Map<String, Integer> getTotalViewCountsByIds(List<String> jobIds) {
		Map<String, Integer> result = new HashMap<>();
		for (String jobId : jobIds) {
			result.put(jobId, getClickCount(jobId));
		}
		return result;
	}
}
