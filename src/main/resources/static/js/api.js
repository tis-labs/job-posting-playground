export async function increaseClickCount(jobId) {
    const card = document.querySelector(`.job-card[data-id="${jobId}"]`);

    if (!card) {
        console.error(`해당 Job ID에 대한 카드 요소를 찾을 수 없습니다.`);
        return;
    }

    let currentViews = parseInt(card.getAttribute('data-views'), 10) || 0;
    let updatedViews = currentViews + 1;

    // 조회수 UI 즉시 업데이트
    card.setAttribute('data-views', updatedViews);
    card.querySelector('.view-count').textContent = `조회수: ${updatedViews}`;

    // 서버에 조회수 증가 요청
    try {
        await fetch("/api/job-postings/click", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: `jobId=${jobId}`
        });
    } catch (error) {
        console.error("조회수 증가 요청 실패:", error);
    }
}
