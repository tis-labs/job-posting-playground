import { applyRandomColors } from "./colors.js";
import { applyLayout } from "./layout.js";
import { addClickEvents } from "./events.js";

document.addEventListener('DOMContentLoaded', () => {
    const container = document.querySelector('.grid-container');
    const cards = document.querySelectorAll('.job-card');

    applyRandomColors(cards);
    applyLayout(cards, container);
    addClickEvents(cards);

    // 1분마다 새로고침하여 조회수 및 크기 반영
    setInterval(() => {
        location.reload();
    }, 60000);
});
