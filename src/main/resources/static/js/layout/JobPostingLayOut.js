export class JobPostingLayoutManager {
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

    calculatePostingPosition(posting, occupiedSpaces, convertedTotalHeight, convertedTotalWidth) {
        let convertedPostHeight = Math.ceil((posting.height - 1) / this.unitHeight);
        let convertedPostWidth = Math.ceil((posting.width - 1) / this.unitWidth);
        for (let y = 0; y <= convertedTotalHeight - convertedPostHeight; y++) {
            for (let x = 0; x <= convertedTotalWidth - convertedPostHeight; x++) {
                if (this.canPlacePosting(x, y, convertedPostWidth, convertedPostHeight, occupiedSpaces)) {
                    this.markOccupied(x, y, convertedPostWidth, convertedPostHeight, occupiedSpaces);
                    return { x, y };
                }
            }
        }
        return null;
    }

    canPlacePosting(x, y, width, height, occupiedSpaces) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                if (occupiedSpaces[i][j]) return false;
            }
        }
        return true;
    }

    markOccupied(x, y, width, height, occupiedSpaces) {
        for (let i = y; i < y + height; i++) {
            for (let j = x; j < x + width; j++) {
                occupiedSpaces[i][j] = true;
            }
        }
    }

    updatePostingLayout(postings) {
        this.containerElement.innerHTML = '';
        let convertedTotalHeight = Math.floor(this.height / this.unitHeight);
        let convertedTotalWidth = Math.floor(this.width / this.unitWidth);
        const occupiedSpaces = Array.from(Array(convertedTotalHeight), () => Array(convertedTotalWidth).fill(false));
        postings.forEach(posting => {
            const position = this.calculatePostingPosition(posting, occupiedSpaces, convertedTotalHeight, convertedTotalWidth);
            if (position) {
                const postingElement = this.createPostingElement(posting, position);
                this.containerElement.appendChild(postingElement);
            }
        });
    }

    createPostingElement(posting, position) {
        const postingCard = document.createElement('div');
        postingCard.className = 'job-card';
        postingCard.style.width = `${posting.width}px`;
        postingCard.style.height = `${posting.height}px`;
        postingCard.style.left = `${position.x * this.unitWidth}px`;
        postingCard.style.top = `${position.y * this.unitHeight}px`;
        postingCard.innerHTML = `
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

        return postingCard;
    }

    resize() {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        this.initializeContainer();
    }
}
