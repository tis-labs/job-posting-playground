package dev.jobposting.playground.event.event;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserClickEvent {
	String userId;
	String jobId;
	LocalDateTime createdAt;
}
