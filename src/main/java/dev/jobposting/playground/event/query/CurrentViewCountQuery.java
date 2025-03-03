package dev.jobposting.playground.event.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CurrentViewCountQuery {
	private Integer limit;

	public CurrentViewCountQuery(Integer limit) {
		this.limit = limit;
	}
}
