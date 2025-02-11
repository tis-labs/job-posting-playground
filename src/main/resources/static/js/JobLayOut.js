/**
 * JobLayoutManager 클래스
 * - 채용 공고 카드의 레이아웃을 관리하는 역할
 * - 공고 카드의 위치를 계산하고, 화면에 렌더링하는 기능 포함
 */
export class JobLayoutManager {
    /**
     * containerElement - 공고 카드가 들어갈 컨테이너 요소
     * unitHeight - 배치할 때의 기본 높이 단위
     * unitWidth - 배치할 때의 기본 너비 단위
     */
    constructor(containerElement, unitHeight, unitWidth) {
        this.containerElement = containerElement;
        this.width = window.innerWidth;  // 현재 화면 너비
        this.height = window.innerHeight; // 현재 화면 높이
        this.unitHeight = unitHeight;
        this.unitWidth = unitWidth;
        this.initializeContainer();
    }

    /**
     * 컨테이너의 기본 스타일을 설정 (크기 및 위치)
     */
    initializeContainer() {
        this.containerElement.style.position = 'relative';
        this.containerElement.style.width = `${this.width}px`;
        this.containerElement.style.height = `${this.height}px`;
    }

    /**
     * 공고 카드를 배치할 위치를 계산
     * posting - 공고 데이터 (크기 정보 포함)
     * occupied - 이미 차지된 공간을 나타내는 배열
     * convertedTotalHeight - 전체 컨테이너의 높이 (배치 단위 기준)
     * convertedTotalWidth - 전체 컨테이너의 너비 (배치 단위 기준)
     * 배치할 위치 좌표 {x, y}, 없으면 null 반환
     */
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

    /**
     * 주어진 위치에 공고 카드를 배치할 수 있는지 확인
     * x - 배치할 X 좌표
     * y - 배치할 Y 좌표
     * width - 공고 카드의 너비
     * height - 공고 카드의 높이
     * occupied - 차지된 공간을 나타내는 배열
     * 배치 가능 여부 (true: 가능, false: 불가능)
     */
    canPlaceJobCard(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                if (occupied[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * 특정 위치를 차지된 공간으로 표시
     * x - 시작 X 좌표
     * y - 시작 Y 좌표
     * width - 공고 카드 너비
     * height - 공고 카드 높이
     * occupied - 차지된 공간 배열
     */
    markOccupied(x, y, width, height, occupied) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                occupied[i][j] = true;
            }
        }
    }

    /**
     * 화면에 공고 카드를 업데이트하여 렌더링
     * postings - 서버에서 받은 공고 데이터 목록
     */
    updateLayout(postings) {
        console.log("[updateLayout] 호출됨, 받은 데이터:", postings);

        if (!postings || postings.length === 0) {
            console.warn("updateLayout: 렌더링할 공고 데이터가 없습니다.");
            return;
        }

        this.containerElement.innerHTML = ''; // 기존 요소 초기화

        let currentX = 0; // 현재 X 좌표
        let currentY = 0; // 현재 Y 좌표
        let rowHeight = 0; // 현재 줄에서 가장 높은 카드의 높이 저장
        let maxY = 0; // 최종적으로 사용된 y 좌표 중 가장 큰 값

        // 크기가 큰 순서대로 정렬 후 배치 (왼쪽 우선)
        postings.sort((a, b) => b.height - a.height || b.width - a.width);

        postings.forEach((posting, index) => {
            console.log(`[updateLayout] 공고 ID: ${posting.id}, Width: ${posting.width}, Height: ${posting.height}`);

            if (currentX + posting.width > this.containerElement.clientWidth) {
                // 줄 바꿈
                currentX = 0;
                currentY += rowHeight;
                rowHeight = 0; // 줄 시작이므로 초기화
            }

            rowHeight = Math.max(rowHeight, posting.height); // 현재 줄에서 가장 높은 공고 업데이트
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
        });

        // `.job-layout` 높이를 공고들이 차지하는 크기로 자동 조정
        this.containerElement.style.height = `${maxY}px`;
        console.log(`[updateLayout] 컨테이너 높이 최적화: ${maxY}px`);

        attachClickEventsToCards();
    }


    /**
     * 공고 카드 요소를 생성
     * posting - 공고 데이터
     * position - 배치할 위치 좌표 {x, y}
     * {HTMLElement} 생성된 공고 카드 요소
     */
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

    /**
     * 창 크기가 변경될 때 컨테이너 크기 조정
     */
    resize() {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
    }
}

/**
 * 공고 카드를 클릭했을 때, 서버에 조회수를 증가시키는 요청을 보냄
 * {Event} event - 클릭 이벤트 객체
 */
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

/**
 * 공고 카드 클릭 이벤트를 모든 카드에 추가
 */
export function attachClickEventsToCards() {
    console.log("attachClickEventsToCards 실행됨");

    const cards = document.querySelectorAll('.job-card');
    cards.forEach(card => {
        card.addEventListener("click", handleCardClick);
    });
}
