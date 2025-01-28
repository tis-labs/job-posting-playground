package dev.jobposting.playground.ui;

import dev.jobposting.playground.domain.JobPosting;
import lombok.Getter;
import lombok.Setter;

@Getter
public class JobCard {
	private final int width;
	private final int height;
	private final int viewCount;
	private Point position;
	private final String title;
	private final String company;
	private final String description;

	public JobCard(JobPosting posting, int width, int height) {
		this.width = width;
		this.height = height;
		this.viewCount = posting.getViewCount();
		this.description = posting.getDescription();
		this.title = posting.getTitle();
		this.company = posting.getCompany();
	}

	@Override
	public String toString() {
		return String.format(
			"{\"width\":\"%s\",\"height\":\"%s\"}",
			width, height
		);
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}
