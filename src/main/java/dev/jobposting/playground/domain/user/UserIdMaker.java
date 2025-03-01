package dev.jobposting.playground.domain.user;

import org.springframework.stereotype.Component;

import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.tsid.TsidFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserIdMaker {
	private final TsidFactory tsidFactory;

	public String generate() {
		return String.valueOf(tsidFactory.create().toLong());
	}
}
