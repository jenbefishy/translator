CREATE TABLE IF NOT EXISTS translation_requests (
                                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   user_ip VARCHAR(255),
                                                   source_text VARCHAR(255),
                                                   target_text VARCHAR(255)
);
