/**
 * 조회수에 따라 카드 크기를 계산하는 함수
 * @param {number} views - 조회수
 * @returns {{width: number, height: number}} - 카드의 너비와 높이
 */
export function calculateSize(views) {
    // 도화지 크기 배열 (A6 ~ A2 사이즈, 조회수에 따라 변화)
    const sizes = [
        { width: 74, height: 105 },  // 조회수 0 (1/16 A6 - 기본 크기)
        { width: 105, height: 148 }, // 조회수 1 (A6)
        { width: 148, height: 210 }, // 조회수 2 (A5)
        { width: 210, height: 297 }, // 조회수 3 (A4)
        { width: 297, height: 420 }, // 조회수 4 (A3)
        { width: 420, height: 594 }  // 조회수 5 (A2 - 최대 크기)
    ];

    // 조회수가 5 이상이면 A2 크기를 유지
    return views >= sizes.length ? sizes[sizes.length - 1] : sizes[views];
}

/**
 * 모든 job-card 요소에 크기를 적용하는 함수
 * @param {NodeList} cards - .job-card 요소 리스트
 * @param {HTMLElement} container - 카드들이 포함된 부모 컨테이너 요소
 */
export function applyLayout(cards, container) {
    cards.forEach(card => {
        // 해당 카드의 조회수 가져오기 (기본값 0)
        const views = parseInt(card.getAttribute('data-views'), 10) || 0;

        // 조회수 기반 크기 계산
        const { width, height } = calculateSize(views);

        // 크기 및 위치 스타일 적용
        card.style.width = `${width}px`;
        card.style.height = `${height}px`;
        card.style.position = "relative";

        console.log(`카드 ID: ${card.getAttribute('data-id')}, 조회수: ${views}, width: ${width}, height: ${height}`);
    });
}
