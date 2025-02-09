package dev.jobposting.playground.domain;

import lombok.Getter;

@Getter
public enum PaperSize {
    DEFAULT(74, 105),
    A2(420, 594),
    A3(420, 297),
    A4( 210, 297),
    A5( 210, 148),
    A6( 105, 148);

    private final int width;
    private final int height;

    PaperSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 조회수에 따라 적절한 크기 반환
     */
    public static PaperSize getSizeByViews(int totalViewCount) {
        if (totalViewCount >= 5) return A2;
        if (totalViewCount >= 4) return A3;
        if (totalViewCount >= 3) return A4;
        if (totalViewCount >= 2) return A5;
        if (totalViewCount >= 1) return A6;
        return DEFAULT;
    }
}
