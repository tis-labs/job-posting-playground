package dev.jobposting.playground.event.service;

import java.time.LocalDateTime;
import java.util.List;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import dev.jobposting.playground.controller.JobPostingResponse;
import dev.jobposting.playground.event.command.UserClickCommand;
import dev.jobposting.playground.event.command.UserOpenCommand;
import dev.jobposting.playground.event.query.CurrentViewCountQuery;
import dev.jobposting.playground.event.query.TotalViewCountQuery;
import dev.jobposting.playground.event.query.ViewCount;
import dev.jobposting.playground.event.query.ViewCountMap;
import dev.jobposting.playground.event.query.ViewCounts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobEventPostingInfoService {
	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;
	public void click(String jobId, String userId) {
		UserClickCommand command = new UserClickCommand(userId, jobId, LocalDateTime.now());
		commandGateway.send(command);
	}

	public void openPosting(String userId) {
		UserOpenCommand command = new UserOpenCommand(userId, "session","sample_place", LocalDateTime.now());
		commandGateway.send(command);
	}

	/**
	 * TODO: 비동기 흐름으로 바꿀 필요 있음
	 */
	public List<JobPostingResponse> getTopViewedJobs(int limit) {
		CurrentViewCountQuery currentViewQuery = new CurrentViewCountQuery(limit);
		ViewCounts currentViewCounts = queryGateway.query(currentViewQuery, ViewCounts.class).join();

		List<String> jobIds = currentViewCounts.viewCounts().stream()
			.map(ViewCount::jobId)
			.toList();

		TotalViewCountQuery totalViewQuery = new TotalViewCountQuery(jobIds);
		ViewCountMap viewCountMap= queryGateway.query(totalViewQuery, ViewCountMap.class).join();
		return currentViewCounts.viewCounts().stream()
			.map(
				viewCount -> new JobPostingResponse(
					viewCount.jobId(),
					viewCount.viewCount(),
					viewCountMap.getViewCount(viewCount.jobId()),
					"샘플 공고 " + viewCount.jobId(),
					"샘플 설명",
					"샘플 회사",
					"default-url"
				)
			)
			.toList();
	}
}
