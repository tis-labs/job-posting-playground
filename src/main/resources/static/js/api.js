import { applyLayout } from "./layout.js";

// 특정 Job ID의 클릭 수(조회수)를 증가시키는 함수 (서버 요청 없이 클라이언트에서 처리)
export function increaseClickCount(jobId, cards) {
    const card = document.querySelector(`.job-card[data-id="${jobId}"]`);

    if (card) {
        let currentViews = parseInt(card.getAttribute('data-views'), 10) || 0;
        let updatedViews = currentViews + 1;

        card.setAttribute('data-views', updatedViews);
        card.querySelector('.view-count').textContent = `조회수: ${updatedViews}`;

        applyLayout(cards, document.querySelector('.grid-container'));
    } else {
        console.error(`해당 Job ID(${jobId})에 대한 카드 요소를 찾을 수 없습니다.`);
    }
}
