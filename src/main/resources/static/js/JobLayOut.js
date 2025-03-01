export class JobLayoutManager {
    constructor(containerElement, unitHeight, unitWidth) {
        this.containerElement = containerElement;
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
        this.initializeContainer();
    }

    // 컨테이너의 크기와 위치 초기화
    initializeContainer() {
        this.containerElement.style.position = 'relative';
        this.containerElement.style.width = `${this.width}px`;
        this.containerElement.style.height = `${this.height}px`;
    }

    // 공고 카드를 배치할 위치를 계산
    calculateJobCardPosition(posting, occupied, convertedTotalHeight, convertedTotalWidth) {
        let convertedPostHeight = Math.ceil(posting.height / this.unitHeight);
        let convertedPostWidth = Math.ceil(posting.width / this.unitWidth);

        // 빈 공간을 찾아 공고를 배치할 위치를 찾음
        for (let y = 0; y <= convertedTotalHeight - convertedPostHeight; y++) {
            for (let x = 0; x <= convertedTotalWidth - convertedPostWidth; x++) {
                if (this.canPlaceJobCard(x, y, convertedPostWidth, convertedPostHeight, occupied)) {
                    this.markOccupied(x, y, convertedPostWidth, convertedPostHeight, occupied);
                    return { x, y };
                }
            }
        }
        return null;
    }

    // 공고 카드를 특정 위치에 배치할 수 있는지 확인
    canPlaceJobCard(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                if (occupied[i][j]) return false;
            }
        }
        return true;
    }

    // 특정 위치를 차지된 공간으로 표시
    markOccupied(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                occupied[i][j] = true;
            }
        }
    }

    // 공고 데이터로 화면에 공고 카드를 업데이트
    updateLayout(postings) {
        if (!postings || postings.length === 0) {
            return;
        }
        window.currentJobCards = postings;
        this.containerElement.innerHTML = '';

        const cards = [];  // 컬러 배열
        let currentX = 0;  // 현재 X 좌표
        let currentY = 0;  // 현재 Y 좌표
        let rowHeight = 0; // 현재 줄에서 가장 높은 카드의 높이 저장
        let maxY = 0;      // 최종적으로 사용된 y 좌표 중 가장 큰 값

        postings.forEach((posting, index) => {
            if (currentX + posting.width > this.containerElement.clientWidth) {
                // 줄 바꿈
                currentX = 0;
                currentY += rowHeight;
                rowHeight = 0; // 줄 시작이므로 초기화
            }

            rowHeight = Math.max(rowHeight, posting.height);  // 현재 줄에서 가장 높은 공고 업데이트
            maxY = Math.max(maxY, currentY + posting.height); // 컨테이너 높이 최적화

            const jobCard = this.createCardElement(posting);
            jobCard.style.width = `${posting.width}px`;
            jobCard.style.height = `${posting.height}px`;

            // 부드러운 이동 애니메이션 적용
            setTimeout(() => {
                jobCard.style.transition = "left 0.3s ease-out, top 0.3s ease-out";
                jobCard.style.left = `${currentX}px`;
                jobCard.style.top = `${currentY}px`;
            }, 50);

            currentX += posting.width; // 다음 카드 위치 업데이트
            this.containerElement.appendChild(jobCard);
            cards.push(jobCard);
        });

        // `.job-layout` 높이를 공고들이 차지하는 크기로 자동 조정
        this.containerElement.style.height = `${maxY}px`;
        applyRandomColors(cards);
        attachClickEventsToCards();
    }

    // 공고 카드 요소를 생성
    createCardElement(posting, position) {
        const jobCard = document.createElement('div');
        jobCard.className = 'job-card';
        jobCard.setAttribute('data-id', posting.id);
        jobCard.setAttribute('data-views', posting.totalViewCount);
        jobCard.setAttribute('data-width', posting.width);
        jobCard.setAttribute('data-height', posting.height);

        let x = position?.x ?? 0;
        let y = position?.y ?? 0;

        jobCard.style.width = `${posting.width}px`;
        jobCard.style.height = `${posting.height}px`;
        jobCard.style.left = `${x * this.unitWidth}px`;
        jobCard.style.top = `${y * this.unitHeight}px`;

        jobCard.innerHTML = `
        <div class="card-content">
            <div class="header-row">
                <div class="job-title">${posting.title}</div>
            </div>
            <div class="job-description">${posting.description}</div>
            <div class="view-count">조회수: ${posting.totalViewCount}</div>
        </div>
    `;

        jobCard.addEventListener("click", handleCardClick);
        return jobCard;
    }

    // 창 크기 변경 시 컨테이너 크기 조정
    resize() {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
    }
}

// 공고 카드 클릭 시 조회수 증가 요청
export async function handleCardClick(event) {
    const card = event.currentTarget;
    const jobId = card.getAttribute("data-id");
    const viewCount = card.getAttribute("data-views");

    try {
        const userId = localStorage.getItem(USER_ID_KEY);
        const response = await fetch(`/api/v1/jobs/${jobId}/view`,
            { method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    id: userId,
                    viewCount: viewCount
                })
            });

        if (response.ok) {
            const updatedData = await response.json();

            // 기존 채용 공고 리스트를 최신 정렬된 데이터로 교체
            window.currentJobCards = updatedData;

            let cards = window.currentJobCards;
            let cardToUpdate = cards.find(card => card.id === parseInt(jobId));
            if (cardToUpdate) {
                let updatedViews = cardToUpdate.totalViewCount;
                let width = cardToUpdate.width;
                let height = cardToUpdate.height;
                cardToUpdate.height = height;
                cardToUpdate.width = width;
                cardToUpdate.totalViewCount = updatedViews;
            }
            jobLayoutManager.updateLayout(cards);
            card.setAttribute("data-views", updatedViews);
            card.querySelector(".view-count").textContent = `조회수: ${updatedViews}`;
        } else {
            console.error("조회수 증가 실패");
        }
    } catch (error) {
        console.error("조회수 증가 요청 중 오류 발생");
    }
}

// 모든 공고 카드에 클릭 이벤트 추가
export function attachClickEventsToCards() {
    const cards = document.querySelectorAll('.job-card');
    cards.forEach(card => {
        card.addEventListener("click", handleCardClick);
    });
}

export const postItColors = ['#FFF6B8', '#FFD3B8', '#A9D4FF', '#FFC2C2', '#F4E2FF'];

export function applyRandomColors(cards) {
    cards.forEach(card => {
        const randomColor = postItColors[Math.floor(Math.random() * postItColors.length)];
        card.style.backgroundColor = randomColor;
    });
}

export const USER_ID_KEY = 'JOB_BOARD_USER_ID';
