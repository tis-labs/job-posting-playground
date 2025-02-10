package dev.jobposting.playground.controller;

import lombok.Getter;

@Getter
public class JobPostingResponse {
	private final Long id;
	private final int fiveMinViewCount;
	private final int totalViewCount;
	private final int clickCount;
	private final String title;
	private final String description;
	private final String company;
	private final String url;

	public JobPostingResponse(Long id, int fiveMinViewCount, int totalViewCount, int clickCount,
							  String title, String description, String company, String url) {
		this.id = id;
		this.fiveMinViewCount = fiveMinViewCount;
		this.totalViewCount = totalViewCount;
		this.clickCount = clickCount;
		this.title = title;
		this.description = description;
		this.company = company;
		this.url = url;
	}
}
