package dev.jobposting.playground.event.projection;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Repository;

import dev.jobposting.playground.event.event.UserClickEvent;
import dev.jobposting.playground.event.query.TotalViewCountQuery;
import dev.jobposting.playground.event.query.ViewCountMap;
import dev.jobposting.playground.service.JobPostingInfoService;
import dev.jobposting.playground.service.TotalViewStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TotalClickProjection {
	private final TotalViewStorage totalViewStorage;

	@EventHandler
	public void on(UserClickEvent userClickEvent) {
		log.info("click event handler by total execute!");
		totalViewStorage.increase(userClickEvent.getJobId());
	}

	@QueryHandler
	public ViewCountMap handle(TotalViewCountQuery query) {
		return new ViewCountMap(totalViewStorage.getTotalViewCountsByIds(query.getJobIds()));
	}

}
