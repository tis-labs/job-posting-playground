package dev.jobposting.playground.domain;

import lombok.Getter;

@Getter
public enum PaperSize {
    DEFAULT(74, 105),
    A6(105, 148),
    A5(148, 210),
    A4(210, 297),
    A3(297, 420),
    A2(420, 594);

    private final int width;
    private final int height;

    PaperSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 조회수에 따라 적절한 크기 반환
     */
    public static PaperSize getSizeByViews(int clickCount) {
        if (clickCount >= 5) return A2;
        if (clickCount >= 4) return A3;
        if (clickCount >= 3) return A4;
        if (clickCount >= 2) return A5;
        if (clickCount >= 1) return A6;
        return DEFAULT;
    }
}
