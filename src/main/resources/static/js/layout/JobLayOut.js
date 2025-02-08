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
        this.containerElement.innerHTML = '';
        let convertedTotalHeight = Math.floor(this.height / this.unitHeight);
        let convertedTotalWidth = Math.floor(this.width / this.unitWidth);
        const occupiedSpaces = Array.from(Array(convertedTotalHeight), () => Array(convertedTotalWidth).fill(false));
        postings.forEach(posting => {
            const position = this.calculateJobCardPosition(posting, occupiedSpaces, convertedTotalHeight, convertedTotalWidth);
            if (position) {
                const postingElement = this.createCardElement(posting, position);
                this.containerElement.appendChild(postingElement);
            }
        });
    }

    createCardElement(posting, position) {
        const jobCard = document.createElement('div');
        jobCard.className = 'job-card';
        jobCard.style.width = `${posting.width}px`;
        jobCard.style.height = `${posting.height}px`;
        jobCard.style.left = `${position.x * this.unitWidth}px`;
        jobCard.style.top = `${position.y * this.unitHeight}px`;
        jobCard.innerHTML = `
            <div class="card-content">
                <div class="header-row">
                    <div class="job-title">${posting.title}</div>
                    <div class="view-count">
                        <i class="fas fa-eye"></i> ${posting.viewCount || 0}
                    </div>
                </div>
                <div class="job-description">${posting.description}</div>
            </div>
        `;

        return jobCard;
    }

    resize() {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
    }
}
