import { JobLayoutManager, handleCardClick, attachClickEventsToCards } from "./JobLayOut.js";

window.jobLayoutManager = new JobLayoutManager(
    document.querySelector('.job-layout'),
    74,
    105
);

if (!window.jobLayoutManager.containerElement) {
    console.error("jobLayoutManager의 컨테이너 요소를 찾을 수 없습니다.");
}

function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function loadJobPostings() {
    console.log("[loadJobPostings] API 요청 시작");
    fetch('/api/v1/jobs')
        .then(response => response.json())
        .then(cards => {
            console.log("[loadJobPostings] API 응답 데이터:", cards);

            if (!cards || cards.length === 0) {
                console.warn("서버에서 받은 공고 데이터가 없습니다.");
                return;
            }

            window.currentJobCards = cards;
            jobLayoutManager.updateLayout(cards);
            attachClickEventsToCards();
        })
        .catch(error => console.error('채용공고 로딩 실패:', error));
}

document.addEventListener('DOMContentLoaded', function() {
    if (!isMobile()) {
        const container = document.getElementById('title-container');
        const titleElement = document.createElement('h1');
        titleElement.textContent = '채용공고';
        titleElement.className = 'title';
        container.appendChild(titleElement);
        loadJobPostings();
    }
});

// 창 크기 변경 이벤트 (레이아웃 업데이트)
window.addEventListener('resize', function () {
    console.log("[resize] 창 크기 변경 감지됨");

    jobLayoutManager.width = window.innerWidth;
    jobLayoutManager.height = window.innerHeight;

    console.log("[resize] 현재 width:", jobLayoutManager.width);
    console.log("[resize] 현재 height:", jobLayoutManager.height);

    // 창 크기 변경 시, 최신 데이터 다시 불러오기
    fetchAndUpdateJobPostings();
});

async function fetchAndUpdateJobPostings() {
    try {
        const response = await fetch("/api/v1/jobs");
        const cards = await response.json();

        console.log("서버에서 받은 데이터:", cards);

        // 크기 업데이트를 위해 레이아웃 갱신
        jobLayoutManager.updateLayout(cards);

        cards.forEach((cardData) => {
            const jobCard = document.querySelector(`.job-card[data-id="${cardData.id}"]`);
            if (jobCard) {
                let updatedViews = cardData.totalViewCount;  // 서버 조회수 반영

                console.log(`[fetchAndUpdateJobPostings] id: ${cardData.id}, 서버 조회수: ${updatedViews}`);

                jobCard.setAttribute("data-views", updatedViews);
                jobCard.querySelector(".view-count").textContent = `조회수: ${updatedViews}`;
            }
        });

    } catch (error) {
        console.error("UI 업데이트 중 오류 발생:", error);
    }
}

// 1분마다 공고 크기 업데이트 (즉시 적용 X, 일정 간격 유지)
setInterval(async () => {
    console.log("[⏳] 1분 후 조회수 및 크기 업데이트 실행됨");
    await fetchAndUpdateJobPostings();
}, 60000);

document.addEventListener('DOMContentLoaded', () => {
    console.log("페이지 로드 완료");

    // 초기 UI 세팅
    setTimeout(fetchAndUpdateJobPostings, 500);  // 0.5초 후 UI 초기화
    setInterval(fetchAndUpdateJobPostings, 60000); // 1분마다 조회수 갱신 60000
});
