// 조회수에 따라 카드 크기 계산
export function calculateSize(views) {
    const sizes = [
        { width: 74, height: 105 },  // 기본 크기 (A6 이하)
        { width: 105, height: 148 }, // A6
        { width: 148, height: 210 }, // A5
        { width: 210, height: 297 }, // A4
        { width: 297, height: 420 }, // A3
        { width: 420, height: 594 }  // 최대 크기 (A2)
    ];

    return views >= sizes.length ? sizes[sizes.length - 1] : sizes[views];
}

// 모든 job-card 요소의 크기 즉시 조정 (조회수 기반)
export function applyLayout(cards, container) {
    cards.forEach(card => {
        const views = parseInt(card.getAttribute('data-views'), 10) || 0;
        const { width, height } = calculateSize(views);

        card.style.width = `${width}px`;
        card.style.height = `${height}px`;
        card.style.position = "relative";
    });
}
