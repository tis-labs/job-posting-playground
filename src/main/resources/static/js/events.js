import { increaseClickCount } from "./api.js";

// 카드 클릭 시 조회수 증가 이벤트 추가
export function addClickEvents(cards) {
    cards.forEach(card => {
        card.addEventListener('click', () => {
            const jobId = card.getAttribute('data-id');
            increaseClickCount(jobId, cards);
        });
    });
}
