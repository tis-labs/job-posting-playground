package dev.jobposting.playground.controller;

import dev.jobposting.playground.ui2.PaperSize;
import lombok.Getter;

@Getter
public class JobCardResponse {
	private final Long id;
	private final int fiveMinViewCount;
	private final int totalViewCount;
	private final String title;
	private final String description;
	private final String company;
	private final String url;
	private final int width;
	private final int height;

	private JobCardResponse(Long id, int fiveMinViewCount, int totalViewCount, String title, String description,
		String company, String url, int width, int height) {
		this.id = id;
		this.fiveMinViewCount = fiveMinViewCount;
		this.totalViewCount = totalViewCount;
		this.title = title;
		this.description = description;
		this.company = company;
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public static JobCardResponse from(JobPostingResponse jobPostingResponse, PaperSize paperSize) {
		return new JobCardResponse(
			jobPostingResponse.getId(),
			jobPostingResponse.getFiveMinViewCount(),
			jobPostingResponse.getTotalViewCount(),
			jobPostingResponse.getTitle(),
			jobPostingResponse.getDescription(),
			jobPostingResponse.getCompany(),
			jobPostingResponse.getUrl(),
			paperSize.getWidth(),
			paperSize.getHeight()
		);
	}
}
