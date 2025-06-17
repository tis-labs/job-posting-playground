package dev.jobposting.playground.event.query;

import java.util.Map;

public record ViewCountMap (Map<String, Integer> viewCountMap){
	public Integer getViewCount(String jobId){
		return viewCountMap.get(jobId);
	}
}
