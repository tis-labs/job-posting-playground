package dev.jobposting.playground.user.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jobposting.playground.domain.user.UserIdMaker;
import dev.jobposting.playground.event.service.JobEventPostingInfoService;
import dev.jobposting.playground.user.config.UserLocation;
import dev.jobposting.playground.user.dto.UserIdResponseDto;
import dev.jobposting.playground.user.dto.UserLocationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserApiController {
	private final UserIdMaker userIdMaker;
	private final JobEventPostingInfoService jobEventPostingInfoService;

	@PostMapping("users/id")
	public UserIdResponseDto issueUserId() {
		String userId = userIdMaker.generate();
		return new UserIdResponseDto(userId);
	}

	@PostMapping("events/users/{id}/postings/open")
	public UserIdResponseDto open(@PathVariable String id
		,  @UserLocation UserLocationDto userLocationDto
	) {
		jobEventPostingInfoService.openPosting(id);
		return new UserIdResponseDto(id);
	}
}
