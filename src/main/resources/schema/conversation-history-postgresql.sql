-- Spring AI 기본 스키마(SPRING_AI_CHAT_MEMORY)를 테이블명만 바꿔서 정의
CREATE TABLE IF NOT EXISTS conversation_history (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    sequence_id BIGINT NOT NULL
    );

CREATE INDEX CONCURRENTLY IF NOT EXISTS conversation_history_conversation_id_created_at_idx
ON conversation_history(conversation_id, created_at);

CREATE INDEX CONCURRENTLY IF NOT EXISTS conversation_history_conversation_id_sequence_id_idx
ON conversation_history(conversation_id, sequence_id);
