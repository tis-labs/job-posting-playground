// 조회수 증가 API 함수 import
import { increaseClickCount } from "./api.js";

/**
 * 각 job-card 요소에 클릭 이벤트 리스너를 추가하는 함수
 * @param {NodeList} cards - .job-card 요소 리스트
 */
export function addClickEvents(cards) {
    cards.forEach(card => {
        card.addEventListener('click', () => {
            // 클릭한 카드의 data-id 값을 가져옴 (Job ID)
            const jobId = card.getAttribute('data-id');

            // 클릭한 카드의 조회수를 증가시키는 함수 호출
            increaseClickCount(jobId, cards);
        });
    });
}
