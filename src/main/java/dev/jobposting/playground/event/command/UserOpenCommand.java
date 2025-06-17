package dev.jobposting.playground.event.command;

import java.time.LocalDateTime;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserOpenCommand {
	@TargetAggregateIdentifier
	String userId;
	String userIdType;
	String residence;
	LocalDateTime createdAt;
}
