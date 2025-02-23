package dev.jobposting.playground.domain.user;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class UuidUserIdMaker implements UserIdMaker {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}
}
