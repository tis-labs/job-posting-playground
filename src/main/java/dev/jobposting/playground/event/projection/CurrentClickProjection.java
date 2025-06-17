package dev.jobposting.playground.event.projection;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Repository;

import dev.jobposting.playground.event.event.UserClickEvent;
import dev.jobposting.playground.event.query.CurrentViewCountQuery;
import dev.jobposting.playground.event.query.ViewCounts;
import dev.jobposting.playground.service.CurrentViewStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CurrentClickProjection {
	private final CurrentViewStorage currentViewStorage;

	@EventHandler
	public void on(UserClickEvent userClickEvent) {
		log.info("click event handler by current execute! " + userClickEvent.getCreatedAt().toString());
		currentViewStorage.increase(userClickEvent.getJobId());
	}

	@QueryHandler
	public ViewCounts findTopCurrentViewedJobs(CurrentViewCountQuery currentViewCountQuery) {
		log.info("current click query handler execute!");
		return new ViewCounts(currentViewStorage.getTopViewedJobsByLimit(currentViewCountQuery.getLimit()));
	}
}
