// 배경색 랜덤 적용 함수 import
import { applyRandomColors } from "./colors.js";
// 카드 크기 계산 및 레이아웃 적용 함수 import
import { applyLayout } from "./layout.js";
// 클릭 이벤트 추가 함수 import
import { addClickEvents } from "./events.js";

/**
 * DOM이 완전히 로드된 후 실행되는 이벤트 리스너
 */
document.addEventListener('DOMContentLoaded', () => {
    // 카드들이 들어있는 부모 컨테이너 요소 선택
    const container = document.querySelector('.grid-container');
    // 모든 job-card 요소 선택
    const cards = document.querySelectorAll('.job-card');

    // 카드 배경색 랜덤 적용
    applyRandomColors(cards);

    // 카드 레이아웃 설정 (조회수 기반 크기 조절)
    applyLayout(cards, container);

    // 카드 클릭 이벤트 추가 (조회수 증가 기능)
    addClickEvents(cards);

    // 창 크기 변경 시 카드 레이아웃을 다시 적용
    window.addEventListener('resize', () => applyLayout(cards, container));
});
