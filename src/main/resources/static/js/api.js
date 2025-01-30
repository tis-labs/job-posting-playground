// layout.js에서 가져온 applyLayout 함수: 조회수 변경 후 카드 크기를 다시 계산하기 위해 사용
import { applyLayout } from "./layout.js";

// API 호출을 통해 특정 Job ID의 클릭 수(조회수)를 증가시키는 함수
export function increaseClickCount(jobId, cards) {
    console.log(`increaseClickCount 호출됨. 대상 Job ID: ${jobId}`);

    // 클릭 수 증가 요청을 서버에 보냄
    fetch(`/api/job-postings/${jobId}/click`, { method: 'POST' })
        .then(response => {
            // 서버 응답이 정상적이지 않으면 오류 처리
            if (!response.ok) {
                throw new Error(`Failed to update click count: ${response.statusText}`);
            }
            return response.json(); // JSON 형식으로 응답 데이터 반환
        })
        .then(updatedJob => {
            console.log(`서버 응답: ${JSON.stringify(updatedJob)}`);

            // 응답 데이터가 올바른지 검증 (job ID와 clickCount가 존재해야 함)
            if (updatedJob && updatedJob.id && updatedJob.clickCount !== undefined) {
                // 조회수가 업데이트될 대상 job-card 요소 찾기
                const card = document.querySelector(`.job-card[data-id="${updatedJob.id}"]`);

                if (card) {
                    // 조회수를 업데이트 (HTML 속성과 화면 표시 텍스트 변경)
                    card.setAttribute('data-views', updatedJob.clickCount);
                    card.querySelector('.view-count').textContent = `조회수: ${updatedJob.clickCount}`;

                    // 카드 크기를 다시 조정하기 위해 applyLayout 호출
                    applyLayout(cards, document.querySelector('.grid-container'));
                }
            } else {
                // 서버 응답 데이터가 예상과 다를 경우 오류 출력 및 사용자에게 알림
                console.error('서버 응답 데이터가 올바르지 않습니다:', updatedJob);
                alert('조회수 데이터를 업데이트할 수 없습니다. 다시 시도해주세요.');
            }
        })
        .catch(error => {
            // API 호출 중 오류 발생 시 콘솔에 로그 출력 및 사용자에게 알림
            console.error('조회수 업데이트 중 오류 발생:', error);
            alert('조회수를 업데이트하는 데 문제가 발생했습니다. 다시 시도해주세요.');
        });
}
