package dev.jobposting.playground.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {
	private final String id;
	private final int viewCount;
}
