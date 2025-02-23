package dev.jobposting.playground.domain.user;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
	private static final String SESSION_ID = "SESSION_ID";
	private final UserIdMaker userIdMaker;

	public void createSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_ID, userIdMaker.generate());
	}

	public String getUserId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new IllegalStateException("세션이 없습니다.");
		}
		return (String) session.getAttribute(SESSION_ID);
	}
}
