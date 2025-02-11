export class JobLayoutManager {
    constructor(containerElement, unitHeight, unitWidth) {
        this.containerElement = containerElement;
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
    }

    initializeContainer() {
        this.containerElement.style.position = 'relative';
        this.containerElement.style.width = `${this.width}px`;
        this.containerElement.style.height = `${this.height}px`;
    }

    calculateJobCardPosition(posting, occupied, convertedTotalHeight, convertedTotalWidth) {
        let convertedPostHeight = Math.ceil(posting.height / this.unitHeight);
        let convertedPostWidth = Math.ceil(posting.width / this.unitWidth);

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

    canPlaceJobCard(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                if (occupied[i][j]) return false;
            }
        }
        return true;
    }

    markOccupied(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                occupied[i][j] = true;
            }
        }
    }

    updateLayout(postings) {
        console.log("[updateLayout] 호출됨, 받은 데이터:", postings);

        if (!postings || postings.length === 0) {
            console.warn("updateLayout: 렌더링할 공고 데이터가 없습니다.");
            return;
        }

        this.containerElement.innerHTML = '';  // 기존 요소 초기화

        let totalCards = postings.length;
        let maxRows = Math.ceil(totalCards / 4); // 4열 배치 기준으로 행 수 계산
        let cardHeight = 200; // 카드 기본 높이
        let layoutHeight = maxRows * cardHeight + 20; // 추가 여백 포함하여 크기 계산

        this.containerElement.style.height = `${layoutHeight}px`;

        postings.forEach(posting => {
            console.log(`[updateLayout] 공고 ID: ${posting.id}, Width: ${posting.width}, Height: ${posting.height}`);

            const jobCard = this.createCardElement(posting);
            jobCard.style.width = `${posting.width}px`;
            jobCard.style.height = `${posting.height}px`;

            this.containerElement.appendChild(jobCard);
        });

        attachClickEventsToCards();
    }

    createCardElement(posting, position) {
        const jobCard = document.createElement('div');
        jobCard.className = 'job-card';
        jobCard.setAttribute('data-id', posting.id);

        let savedViews = localStorage.getItem(`job-${posting.id}-views`);
        let initialViews = savedViews !== null ? parseInt(savedViews, 10) : 0;

        jobCard.setAttribute('data-views', initialViews);
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
            <div class="view-count">조회수: ${initialViews}</div>
        </div>
    `;

        jobCard.addEventListener("click", handleCardClick);
        return jobCard;
    }

    resize() {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
    }
}

export async function handleCardClick(event) {
    const card = event.currentTarget;
    const jobId = card.getAttribute("data-id");

    console.log(`[handleCardClick] 공고 ID ${jobId} 클릭됨`);

    try {
        const response = await fetch(`/api/v1/jobs/${jobId}/view`, { method: "POST" });

        if (response.ok) {
            const updatedData = await response.json();
            let updatedViews = updatedData.totalViewCount;

            console.log(`[handleCardClick] 서버 응답 조회수: ${updatedViews}`);

            card.setAttribute("data-views", updatedViews);
            card.querySelector(".view-count").textContent = `조회수: ${updatedViews}`;
        } else {
            console.error("[handleCardClick] 조회수 증가 실패", response.status);
        }
    } catch (error) {
        console.error("[handleCardClick] 조회수 증가 요청 중 오류 발생", error);
    }
}

// 클릭 이벤트 등록 함수 수정
export function attachClickEventsToCards() {
    console.log("attachClickEventsToCards 실행됨");

    const cards = document.querySelectorAll('.job-card');
    cards.forEach(card => {
        card.addEventListener("click", handleCardClick);
    });
}
