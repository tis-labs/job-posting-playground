package dev.jobposting.playground.event.command;

import java.time.LocalDateTime;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserClickCommand {
	@TargetAggregateIdentifier
	String userId;
	String jobId;
	LocalDateTime createdAt;
}
