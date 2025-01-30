import { PaperSize } from './card/PaperSize.js';
import { JobPostingLayoutManager } from "./layout/JobPostingLayOut.js";

const jobPostingLayout = new JobPostingLayoutManager(
    document.querySelector('.job-layout'),
    PaperSize.UNIT_HEIGHT,
    PaperSize.UNIT_WIDTH
);

// UserAgent로 체크
function isMobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function loadJobPostings() {
    fetch('/api/v1/jobs')
        .then(response => response.json())
        .then(postings => {
            postings.forEach(posting => {
                let paperSize = PaperSize.random();
                posting.width = paperSize.width;
                posting.height = paperSize.height;
            })
            window.currentJobPostings = postings;
            jobPostingLayout.updatePostingLayout(postings);
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
    jobPostingLayout.width = window.innerWidth;
    jobPostingLayout.height = window.innerHeight;
    jobPostingLayout.updatePostingLayout(window.currentJobPostings);
})
