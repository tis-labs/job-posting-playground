package dev.jobposting.playground.event.query;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TotalViewCountQuery {
	private List<String> jobIds;

	public TotalViewCountQuery(List<String> jobIds) {
		this.jobIds = jobIds;
	}
}
