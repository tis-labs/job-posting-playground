package dev.jobposting.playground.controller;

import lombok.Getter;

@Getter
public class JobPostingResponse {
	private final Long id;
	private final int viewCount;
	private final String title;
	private final String description;
	private final String company;
	private final String url;

	public JobPostingResponse(Long id, int viewCount, String title, String description, String company, String url) {
		this.id = id;
		this.viewCount = viewCount;
		this.title = title;
		this.description = description;
		this.company = company;
		this.url = url;
	}
}
