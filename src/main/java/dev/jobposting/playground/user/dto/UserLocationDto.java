package dev.jobposting.playground.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLocationDto {
	private final double latitude;
	private final double longitude;
}
