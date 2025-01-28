package dev.jobposting.playground.ui;

import java.util.List;

public class Board {
	private final int width;
	private final int height;
	private boolean[][] occupied;

	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.occupied = new boolean[height][width];
	}

	private boolean placeCard(JobCard card) {
		for (int y = 0; y <= height - card.getHeight(); y++) {
			for (int x = 0; x <= width - card.getWidth(); x++) {
				if (canPlace(x, y, card)) {
					place(x, y, card);
					card.setPosition(new Point(x, y));
					return true;
				}
			}
		}
		return false;
	}

	private boolean canPlace(int x, int y, JobCard posting) {
		for (int i = y; i < y + posting.getHeight(); i++) {
			for (int j = x; j < x + posting.getWidth(); j++) {
				if (occupied[i][j]) return false;
			}
		}
		return true;
	}

	private void place(int x, int y, JobCard posting) {
		for (int i = y; i < y + posting.getHeight(); i++) {
			for (int j = x; j < x + posting.getWidth(); j++) {
				occupied[i][j] = true;
			}
		}
	}

	public void placeCards(List<JobCard> postings) {
		for (JobCard posting : postings) {
			placeCard(posting);
		}
	}
}
