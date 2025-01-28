function updateBoardSize() {
    const width = window.innerWidth;
    const height = window.innerHeight;

    // 기존 카드들을 먼저 제거
    const board = document.querySelector('.board');
    board.innerHTML = '';

    fetch('/jobs/updateSize', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `width=${width}&height=${height}`
    })
        .then(response => response.json())
        .then(postings => {
            updatePostingsDisplay(postings);
        })
        .catch(error => console.error('Error:', error));
}

function updatePostingsDisplay(postings) {
    const board = document.querySelector('.board');
    board.innerHTML = '';

    postings.forEach(posting => {
        const cardDiv = document.createElement('div');
        cardDiv.className = 'job-card';
        cardDiv.style.width = `${posting.width}px`;
        cardDiv.style.height = `${posting.height}px`;
        cardDiv.style.left = `${posting.position.x}px`;
        cardDiv.style.top = `${posting.position.y}px`;
        cardDiv.innerHTML = `
            <div class="card-content">
                <div class="header-row">
                    <div class="job-title">${posting.title}</div>
                    <div class="view-count">
                        <i class="fas fa-eye"></i>
                        ${posting.viewCount || 0}
                    </div>
                </div>
                <div class="job-description">${posting.description}</div>
            </div>
        `;
        board.appendChild(cardDiv);
    });
}


function debounce(func, wait, immediate = false) {
    let timeout;

    return function executedFunction(...args) {
        const context = this;
        const later = () => {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

const debouncedUpdateBoardSize = debounce(updateBoardSize, 100, true);
window.addEventListener('resize', debouncedUpdateBoardSize);
window.addEventListener('load', updateBoardSize);
document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('title-container');
    const titleElement = document.createElement('h1');
    titleElement.textContent = '채용공고';
    titleElement.className = 'title';
    container.appendChild(titleElement);
});
