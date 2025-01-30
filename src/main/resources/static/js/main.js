import { applyRandomColors } from "./colors.js";
import { applyLayout } from "./layout.js";
import { addClickEvents } from "./events.js";

// 페이지 로드 후 실행
document.addEventListener('DOMContentLoaded', () => {
    const container = document.querySelector('.grid-container');
    const cards = document.querySelectorAll('.job-card');

    applyRandomColors(cards);

    applyLayout(cards, container);

    addClickEvents(cards);

    window.addEventListener('resize', () => applyLayout(cards, container));
});
