package dev.jobposting.playground.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import dev.jobposting.playground.domain.user.SessionService;
import dev.jobposting.playground.domain.user.UserIdMaker;
import dev.jobposting.playground.domain.user.UuidUserIdMaker;
import jakarta.servlet.http.HttpServletRequest;

public class SessionServiceTest {
	private UserIdMaker userIdMaker;
	private HttpServletRequest httpServletRequest;
	private SessionService sessionService;

	@BeforeEach
	void setUp() {
		userIdMaker = new UuidUserIdMaker();
		httpServletRequest = new MockHttpServletRequest();
		sessionService = new SessionService(userIdMaker);
	}

	@Test
	@DisplayName("세션이 생성되면 세션 조회 시 사용자 아이디를 반환한다.")
	void whenSessionIsCreated_thenUserIdisNotNull() {
		//given
		sessionService.createSession(httpServletRequest);

		//when
		String userId = sessionService.getUserId(httpServletRequest);

		//then
		assertNotNull(userId);
	}

	@Test
	@DisplayName("세션이 생성되지 않으면 세션 조회 시 예외를 던진다.")
	void whenSessionIsNotCreated_thenUserIdisNull() {
		//given, when, then
		assertThrows(IllegalStateException.class, () -> sessionService.getUserId(httpServletRequest));
	}
}
