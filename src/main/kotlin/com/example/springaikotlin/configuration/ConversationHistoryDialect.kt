package com.example.springaikotlin.configuration

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect

/**
 * Spring AI 기본 Dialect는 테이블명(SPRING_AI_CHAT_MEMORY)이 하드코딩되어 있고 변경 프로퍼티(table-name)도 없으므로,
 * 테이블명만 바꾼 PostgreSQL용 Dialect를 직접 정의한다.
 * 테이블명을 바꿀 경우 schema/conversation-history-postgresql.sql 도 함께 수정해야 한다.
 *
 * 기본 스키마의 "timestamp" 컬럼은 created_at으로 변경했다.
 * Spring AI는 조회 결과를 컬럼명이 아닌 순서(content, type, 시각)로 읽으므로 SELECT 컬럼 순서는 유지해야 한다.
 */
class ConversationHistoryDialect : JdbcChatMemoryRepositoryDialect {

    override fun getSelectMessagesSql(): String =
        """SELECT content, type, created_at FROM $TABLE_NAME WHERE conversation_id = ? ORDER BY sequence_id"""

    override fun getInsertMessageSql(): String =
        """INSERT INTO $TABLE_NAME (conversation_id, content, type, created_at, sequence_id) VALUES (?, ?, ?, ?, ?)"""

    override fun getSelectConversationIdsSql(): String =
        "SELECT DISTINCT conversation_id FROM $TABLE_NAME"

    override fun getDeleteMessagesSql(): String =
        "DELETE FROM $TABLE_NAME WHERE conversation_id = ?"

    companion object {
        const val TABLE_NAME = "conversation_history"
    }
}
