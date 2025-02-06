import { PaperSize } from './card/PaperSize.js';
import { JobLayoutManager } from "./layout/JobLayOut.js";

const jobLayoutManager = new JobLayoutManager(
    document.querySelector('.job-layout'),
    PaperSize.UNIT_HEIGHT,
    PaperSize.UNIT_WIDTH
);

function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function loadJobPostings() {
    fetch('/api/v1/jobs')
        .then(response => response.json())
        .then(cards => {
            window.currentJobCards = cards;
            jobLayoutManager.updateLayout(cards);
        })
        .catch(error => console.error('채용공고 로딩 실패:', error));
}

document.addEventListener('DOMContentLoaded', function() {
    if (isMobile()) {

    } else {
        const container = document.getElementById('title-container');
        const titleElement = document.createElement('h1');
        titleElement.textContent = '채용공고';
        titleElement.className = 'title';
        container.appendChild(titleElement);
        loadJobPostings();
    }
});

window.addEventListener('resize', function () {
    jobLayoutManager.width = window.innerWidth;
    jobLayoutManager.height = window.innerHeight;
    jobLayoutManager.updateLayout(window.currentJobCards);
})
