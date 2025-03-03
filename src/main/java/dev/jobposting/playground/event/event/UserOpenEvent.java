package dev.jobposting.playground.event.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserOpenEvent {
	String userId;
	String userIdType;
	String residence;
	LocalDateTime createdAt;
}
