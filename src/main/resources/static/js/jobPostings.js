import { JobLayoutManager, handleCardClick, attachClickEventsToCards } from "./JobLayOut.js";

/**
 * JobLayoutManager 인스턴스 생성
 * - 채용 공고를 배치하는 역할
 * - '.job-layout' 컨테이너 안에 공고 카드를 관리
 * - unitHeight(74px), unitWidth(105px) 크기 단위 사용
 */
window.jobLayoutManager = new JobLayoutManager(
    document.querySelector('.job-layout'),
    105,
    148
);

// 컨테이너 요소가 정상적으로 생성되지 않았을 경우 오류 출력
if (!window.jobLayoutManager.containerElement) {
    console.error("jobLayoutManager의 컨테이너 요소를 찾을 수 없습니다.");
}

/**
 * 서버에서 채용 공고 데이터를 가져오는 함수
 * - '/api/v1/jobs' 엔드포인트에서 데이터를 가져옴
 * - 가져온 데이터를 화면에 렌더링 (updateLayout 함수 호출)
 */
function loadJobPostings() {
    console.log("[loadJobPostings] API 요청 시작");

    fetch('/api/v1/jobs')
        .then(response => response.json())  // JSON 형태로 변환
        .then(cards => {
            console.log("[loadJobPostings] API 응답 데이터:", cards);

            if (!cards || cards.length === 0) {
                console.warn("서버에서 받은 공고 데이터가 없습니다.");
                return;
            }

            // 전역 변수에 현재 공고 리스트 저장
            window.currentJobCards = cards;

            // 공고 레이아웃 업데이트
            jobLayoutManager.updateLayout(cards);

            // 공고 카드 클릭 이벤트 추가
            attachClickEventsToCards();
        })
        .catch(error => console.error('채용공고 로딩 실패:', error));
}

/**
 * DOMContentLoaded 이벤트 리스너
 * - 모바일이 아닌 환경에서만 실행됨
 * - 페이지가 로드되면 제목 추가 후 공고 데이터를 불러옴
 */
document.addEventListener('DOMContentLoaded', function() {
    if (!isMobile()) {
        // 제목을 추가할 컨테이너 가져오기
        const container = document.getElementById('title-container');

        // '채용공고' 제목 추가
        const titleElement = document.createElement('h1');
        titleElement.textContent = '채용공고';
        titleElement.className = 'title';
        container.appendChild(titleElement);

        // 채용 공고 데이터 불러오기
        loadJobPostings();
    }
});

/**
 * 창 크기가 변경될 때 호출되는 이벤트 핸들러
 * - 화면 크기에 맞춰 레이아웃을 업데이트
 */
window.addEventListener('resize', function () {
    console.log("[resize] 창 크기 변경 감지됨");

    // 현재 화면 크기로 jobLayoutManager 크기 업데이트
    jobLayoutManager.width = window.innerWidth;
    jobLayoutManager.height = window.innerHeight;

    console.log("[resize] 현재 width:", jobLayoutManager.width);
    console.log("[resize] 현재 height:", jobLayoutManager.height);

    // 창 크기가 변경되었을 때 공고 데이터를 다시 불러와서 UI 업데이트
    fetchAndUpdateJobPostings();
});

/**
 * 서버에서 최신 공고 데이터를 가져와 UI를 업데이트하는 함수
 * - 서버에서 새 데이터를 가져온 후 updateLayout()을 호출하여 변경 사항을 반영
 * - 조회수도 최신 데이터로 갱신
 */
async function fetchAndUpdateJobPostings() {
    try {
        const response = await fetch("/api/v1/jobs"); // 서버에서 최신 공고 데이터 요청
        const cards = await response.json();  // JSON 변환

        console.log("서버에서 받은 데이터:", cards);

        // 공고 레이아웃 업데이트
        jobLayoutManager.updateLayout(cards);

        // 각 공고 카드의 조회수를 최신 상태로 갱신
        cards.forEach((cardData) => {
            const jobCard = document.querySelector(`.job-card[data-id="${cardData.id}"]`);
            if (jobCard) {
                let updatedViews = cardData.totalViewCount;  // 서버 조회수 데이터 반영

                console.log(`[fetchAndUpdateJobPostings] id: ${cardData.id}, 서버 조회수: ${updatedViews}`);

                // HTML 요소에 조회수 업데이트
                jobCard.setAttribute("data-views", updatedViews);
                jobCard.querySelector(".view-count").textContent = `조회수: ${updatedViews}`;
            }
        });

    } catch (error) {
        console.error("UI 업데이트 중 오류 발생:", error);
    }
}

/**
 * 일정 주기마다 공고 데이터 업데이트 (1분마다 실행)
 * - 1분마다 fetchAndUpdateJobPostings() 호출하여 최신 데이터 반영
 */
setInterval(async () => {
    console.log("1분 후 조회수 및 크기 업데이트 실행됨");
    await fetchAndUpdateJobPostings();
}, 60000);

/**
 * 페이지가 처음 로드될 때 실행되는 초기 설정 함수
 * - 'jobBoard' 요소의 크기 설정
 * - 0.5초 후 fetchAndUpdateJobPostings() 실행 (첫 번째 데이터 로딩)
 * - 이후 1분마다 데이터 갱신
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log("페이지 로드 완료");

    // 'jobBoard' 요소 가져오기
    const jobBoard = document.getElementById("jobBoard");

    // 기본 크기 설정
    jobBoard.style.width = "1200px";
    jobBoard.style.minHeight = "700px";
    jobBoard.style.maxHeight = "90vh";

    // 0.5초 후 첫 데이터 로딩 실행
    setTimeout(fetchAndUpdateJobPostings, 500);

    // 1분마다 데이터 갱신
    setInterval(fetchAndUpdateJobPostings, 60000);
});
