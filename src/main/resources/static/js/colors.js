export const postItColors = ['#FFF6B8', '#FFD3B8', '#A9D4FF', '#FFC2C2', '#F4E2FF'];

// 카드 리스트에 랜덤 배경색 적용
export function applyRandomColors(cards) {
    cards.forEach(card => {
        const randomColor = postItColors[Math.floor(Math.random() * postItColors.length)];
        card.style.backgroundColor = randomColor;
    });
}
