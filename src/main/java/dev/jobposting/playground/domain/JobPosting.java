package dev.jobposting.playground.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JobPosting {
	private int viewCount;
	private String title;
	private String description;
	private String company;
	private String url;

	public JobPosting(int viewCount, String title, String description, String company, String url) {
		this.viewCount = viewCount;
		this.title = title;
		this.description = description;
		this.company = company;
		this.url = url;
	}
}
