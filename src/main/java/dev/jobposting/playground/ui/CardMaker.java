package dev.jobposting.playground.ui;

import java.util.ArrayList;
import java.util.List;

import dev.jobposting.playground.domain.JobPosting;

public class CardMaker {
	public static List<JobCard> makeCard(List<JobPosting> posts) {
		List<JobCard> cards = new ArrayList<>();
		for (int order = 0; order < posts.size(); order++) {
			JobPosting post = posts.get(order);
			PaperSize pageSize = PaperSize.fromOrder(order);
			cards.add(new JobCard(post, pageSize.getWidth(), pageSize.getHeight()));
		}
		return cards;
	}
}
