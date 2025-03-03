package dev.jobposting.playground.event.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import dev.jobposting.playground.event.command.UserClickCommand;
import dev.jobposting.playground.event.command.UserOpenCommand;
import dev.jobposting.playground.event.event.UserClickEvent;
import dev.jobposting.playground.event.event.UserOpenEvent;
import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class UserAggregate {
	@AggregateIdentifier
	private String userId;
	private String type;
	private String residence;

	public UserAggregate() {
	}

	@CommandHandler
	public UserAggregate(UserOpenCommand userOpenCommand) {
		log.info("open command exists: {}", userOpenCommand.getUserId());
		AggregateLifecycle.apply(new UserOpenEvent(
			userOpenCommand.getUserId(),
			userOpenCommand.getUserIdType(),
			userOpenCommand.getResidence(),
			userOpenCommand.getCreatedAt()
			));
	}

	@CommandHandler
	public void handle(UserClickCommand userClickCommand) {
		log.info("click command exists: {}", userClickCommand.getUserId());
		AggregateLifecycle.apply(new UserClickEvent(
			userClickCommand.getUserId(),
			userClickCommand.getJobId(),
			userClickCommand.getCreatedAt()
		));
	}

	@EventSourcingHandler
	public void on(UserOpenEvent userOpenEvent) {
		this.userId = userOpenEvent.getUserId();
		this.type = userOpenEvent.getUserIdType();
		this.residence = userOpenEvent.getResidence();
	}

	@EventSourcingHandler
	public void on(UserClickEvent userClickEvent) {
		this.userId = userClickEvent.getUserId();
	}
}
