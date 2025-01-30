CREATE TABLE job_posting (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    company     VARCHAR(255) NOT NULL,
    location    VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    click_count INT DEFAULT 0
);
