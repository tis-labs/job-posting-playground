// 포스트잇 스타일 배경색 배열 (랜덤 색상 적용을 위해 사용)
export const postItColors = ['#FFF6B8', '#FFD3B8', '#A9D4FF', '#FFC2C2', '#F4E2FF'];

/**
 * 카드 리스트에 랜덤 배경색을 적용하는 함수
 * @param {NodeList} cards - .job-card 요소 리스트
 */
export function applyRandomColors(cards) {
    cards.forEach(card => {
        // postItColors 배열에서 랜덤한 색상을 선택
        const randomColor = postItColors[Math.floor(Math.random() * postItColors.length)];

        // 선택한 색상을 카드의 배경색으로 설정
        card.style.backgroundColor = randomColor;
    });
}
