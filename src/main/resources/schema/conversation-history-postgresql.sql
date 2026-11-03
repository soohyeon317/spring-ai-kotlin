-- Spring AI 기본 스키마(SPRING_AI_CHAT_MEMORY)를 기반으로 대화 종류(ConversationType)별 테이블을 정의

-- 일반 상담 (ConversationType.GENERAL)
CREATE TABLE IF NOT EXISTS general_conversation_history (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    sequence_id BIGINT NOT NULL
    );

CREATE INDEX CONCURRENTLY IF NOT EXISTS general_conversation_history_conversation_id_created_at_idx
ON general_conversation_history(conversation_id, created_at);

CREATE INDEX CONCURRENTLY IF NOT EXISTS general_conversation_history_conversation_id_sequence_id_idx
ON general_conversation_history(conversation_id, sequence_id);

-- 고객지원 (ConversationType.SUPPORT)
CREATE TABLE IF NOT EXISTS support_conversation_history (
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    sequence_id BIGINT NOT NULL
    );

CREATE INDEX CONCURRENTLY IF NOT EXISTS support_conversation_history_conversation_id_created_at_idx
ON support_conversation_history(conversation_id, created_at);

CREATE INDEX CONCURRENTLY IF NOT EXISTS support_conversation_history_conversation_id_sequence_id_idx
ON support_conversation_history(conversation_id, sequence_id);
