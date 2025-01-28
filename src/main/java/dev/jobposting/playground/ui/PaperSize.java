package dev.jobposting.playground.ui;

import lombok.Getter;

@Getter
public enum PaperSize {
	A3("A3", 420, 297),
	A4("A4", 210, 297),
	A5("A5", 210, 148),
	A6("A6", 105, 148),
	A7("A7", 105, 74),
	A8("A8", 53, 74);

	private final String name;
	private final int width;
	private final int height;

	PaperSize(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	public static PaperSize fromOrder(int order) {
		return switch (order) {
			case 0 -> A3;
			case 1 -> A4;
			case 2 -> A5;
			default -> A6;
		};
	}
}
