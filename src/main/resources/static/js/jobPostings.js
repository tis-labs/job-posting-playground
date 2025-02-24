import { JobLayoutManager, handleCardClick, attachClickEventsToCards, USER_ID_KEY } from "./JobLayOut.js";
// JobLayoutManager 인스턴스 생성 (공고 레이아웃 관리)
window.jobLayoutManager = new JobLayoutManager(
    document.querySelector('.job-layout'),
    105,
    148
);

// 컨테이너 요소가 정상적으로 생성되지 않았을 경우 오류 출력
if (!window.jobLayoutManager.containerElement) {
    console.error("컨테이너 요소를 찾을 수 없습니다.");
}

function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

// 서버에서 채용 공고 데이터를 가져오는 함수
function loadJobPostings() {
    fetch('/api/v1/jobs')
        .then(response => response.json())  // JSON 형태로 변환
        .then(cards => {

            if (!cards || cards.length === 0) {
                return;
            }

            // 공고 레이아웃 업데이트
            jobLayoutManager.updateLayout(cards);

            // 공고 카드 클릭 이벤트 추가
            attachClickEventsToCards();
        })
        .catch(error => console.error('채용공고 로딩 실패:', error));
}

// 창 크기 변경 시 공고 레이아웃 업데이트
window.addEventListener("resize", async function () {
    jobLayoutManager.width = window.innerWidth;
    jobLayoutManager.height = window.innerHeight;

    try {
        await fetchAndUpdateJobPostings();
    } catch (error) {
        console.error("채용 공고 업데이트 중 오류 발생:", error);
    }
});

// 서버에서 최신 공고 데이터를 가져와 UI를 업데이트하는 함수
async function fetchAndUpdateJobPostings() {
    try {
        const response = await fetch("/api/v1/jobs");
        const cards = await response.json();  // JSON 변환

        // 공고 레이아웃 업데이트
        jobLayoutManager.updateLayout(cards);

        // 각 공고 카드의 조회수를 최신 상태로 갱신
        cards.forEach((cardData) => {
            const jobCard = document.querySelector(`.job-card[data-id="${cardData.id}"]`);
            if (jobCard) {
                let updatedViews = cardData.totalViewCount; // 서버 조회수 데이터 반영
                // HTML 요소에 조회수 업데이트
                jobCard.setAttribute("data-views", updatedViews);
                jobCard.querySelector(".view-count").textContent = `조회수: ${updatedViews}`;
            }
        });

    } catch (error) {
        console.error("UI 업데이트 중 오류 발생:", error);
    }
}

// 1분마다 공고 데이터 갱신
setInterval(async () => {
    await fetchAndUpdateJobPostings();
}, 60000);

function issueUserId() {
    return fetch('/api/users/id', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            localStorage.setItem(USER_ID_KEY, data.userId);
            return data.userId;
        })
        .catch(error => {
            console.error('사용자 ID 발급 실패:', error);
            throw error;
        });
}

async function sendOpenEvent(userId) {
    try {
        const response = await fetch(`/api/events/users/${userId}/postings/open`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return response.json();
    } catch (error) {
        console.error('열람 이벤트 전송 실패:', error);
        throw error;
    }
}

// 페이지 로드 후 기본 설정 및 첫 데이터 로딩
window.onload = () => {
    issueUserId().then(id => {
        sendOpenEvent(id).then( json => {
            }
        );
    });
    if (!isMobile()) {
        const jobBoard = document.getElementById("jobBoard");

        // 기본 크기 설정
        jobBoard.style.width = "1200px";
        jobBoard.style.minHeight = "700px";
        jobBoard.style.maxHeight = "90vh";

        loadJobPostings(); // 동기 로딩
        setInterval(fetchAndUpdateJobPostings, 60000);
    }
};
