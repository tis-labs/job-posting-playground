import { applyRandomColors } from "./colors.js";

// 서버에서 최신 데이터를 가져와 UI 업데이트 (1분마다 실행)
async function fetchAndUpdateJobPostings() {
    try {
        const response = await fetch("/"); // 서버에서 최신 HTML 가져오기
        const text = await response.text(); // 응답을 텍스트(HTML)로 변환

        const parser = new DOMParser();
        const doc = parser.parseFromString(text, "text/html");
        const jobElements = doc.querySelectorAll(".job-card");

        jobElements.forEach((newCard) => {
            const id = newCard.getAttribute("data-id");
            const views = newCard.getAttribute("data-views");
            const width = newCard.getAttribute("data-width");
            const height = newCard.getAttribute("data-height");

            const oldCard = document.querySelector(`.job-card[data-id="${id}"]`);
            if (oldCard) {
                oldCard.setAttribute("data-views", views);
                oldCard.setAttribute("data-width", width);
                oldCard.setAttribute("data-height", height);

                // 크기 업데이트는 1분마다 적용
                oldCard.style.width = `${width}px`;
                oldCard.style.height = `${height}px`;

                oldCard.querySelector(".view-count").textContent = `조회수: ${views}`;
            }
        });
    } catch (error) {
        console.error("UI 업데이트 중 오류 발생:", error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.job-card');

    applyRandomColors(cards); // JS에서 배경색 랜덤 적용

    cards.forEach(card => {
        card.style.width = `${card.getAttribute('data-width')}px`;
        card.style.height = `${card.getAttribute('data-height')}px`;

        // 클릭 시 조회수 즉시 반영
        card.addEventListener('click', async () => {
            const jobId = card.getAttribute('data-id');
            await increaseClickCount(jobId, card);
        });
    });

    // 1분마다 UI 업데이트 (크기만 변경)
    setInterval(fetchAndUpdateJobPostings, 60000);
});

// 클릭 시 조회수 즉시 증가
async function increaseClickCount(jobId, card) {
    try {
        const response = await fetch(`/job/${jobId}/click`, {
            method: "POST"
        });

        if (response.ok) {
            let currentViews = parseInt(card.getAttribute('data-views'), 10) || 0;
            let updatedViews = currentViews + 1;

            card.setAttribute('data-views', updatedViews);
            card.querySelector('.view-count').textContent = `조회수: ${updatedViews}`;
            // 크기 업데이트는 하지 않고, 1분 후 자동 반영됨
        } else {
            console.error("조회수 증가 실패:", response.status);
        }
    } catch (error) {
        console.error("조회수 증가 요청 오류:", error);
    }
}
