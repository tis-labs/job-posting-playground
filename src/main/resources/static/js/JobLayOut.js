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
        let convertedPostHeight = Math.ceil((posting.height - 1) / this.unitHeight);
        let convertedPostWidth = Math.ceil((posting.width - 1) / this.unitWidth);
        for (let y = 0; y <= convertedTotalHeight - convertedPostHeight; y++) {
            for (let x = 0; x <= convertedTotalWidth - convertedPostHeight; x++) {
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

        let convertedTotalHeight = Math.floor(this.height / this.unitHeight);
        let convertedTotalWidth = Math.floor(this.width / this.unitWidth);
        const occupiedSpaces = Array.from(Array(convertedTotalHeight), () => Array(convertedTotalWidth).fill(false));

        postings.forEach(posting => {
            console.log(`[updateLayout] 공고 ID: ${posting.id}, Width: ${posting.width}, Height: ${posting.height}`);

            const position = this.calculateJobCardPosition(posting, occupiedSpaces, convertedTotalHeight, convertedTotalWidth);
            if (position) {
                const jobCard = this.createCardElement(posting, position);
                this.containerElement.appendChild(jobCard);
            } else {
                console.warn(`[updateLayout] 공고 ID ${posting.id} 위치 계산 실패`);
            }
        });
    }

    createCardElement(posting, position) {
        const jobCard = document.createElement('div');
        jobCard.className = 'job-card';
        jobCard.setAttribute('data-id', posting.id);

        // 조회수 초기값을 0으로 설정
        let savedViews = localStorage.getItem(`job-${posting.id}-views`);
        let initialViews = savedViews !== null ? parseInt(savedViews, 10) : 0;

        jobCard.setAttribute('data-views', initialViews);
        jobCard.setAttribute('data-width', posting.width);
        jobCard.setAttribute('data-height', posting.height);

        jobCard.style.width = `${posting.width}px`;
        jobCard.style.height = `${posting.height}px`;
        jobCard.style.left = `${position.x * this.unitWidth}px`;
        jobCard.style.top = `${position.y * this.unitHeight}px`;

        jobCard.innerHTML = `
        <div class="card-content">
            <div class="header-row">
                <div class="job-title">${posting.title}</div>
            </div>
            <div class="job-description">${posting.description}</div>
            <div class="view-count">조회수: ${initialViews}</div>  <!-- 초기값 0으로 설정 -->
        </div>
    `;

        // 클릭 시 조회수 증가 요청
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
        card.removeEventListener("click", handleCardClick); // 중복 등록 방지
        card.addEventListener("click", handleCardClick);
    });
}
