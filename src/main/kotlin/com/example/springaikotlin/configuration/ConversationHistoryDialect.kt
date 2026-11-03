package com.example.springaikotlin.configuration

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect

/**
 * Spring AI 기본 Dialect는 테이블명(SPRING_AI_CHAT_MEMORY)이 하드코딩되어 있고 변경 프로퍼티(table-name)도 없으므로,
 * 테이블명을 지정할 수 있는 PostgreSQL용 Dialect를 직접 정의한다. (대화 종류별 테이블마다 인스턴스 1개)
 *
 * 기본 스키마의 "timestamp" 컬럼은 created_at으로 변경했다.
 * Spring AI는 조회 결과를 컬럼명이 아닌 순서(content, type, 시각)로 읽으므로 SELECT 컬럼 순서는 유지해야 한다.
 */
class ConversationHistoryDialect(
    private val tableName: String,
) : JdbcChatMemoryRepositoryDialect {

    override fun getSelectMessagesSql(): String =
        "SELECT content, type, created_at FROM $tableName WHERE conversation_id = ? ORDER BY sequence_id"

    override fun getInsertMessageSql(): String =
        "INSERT INTO $tableName (conversation_id, content, type, created_at, sequence_id) VALUES (?, ?, ?, ?, ?)"

    override fun getSelectConversationIdsSql(): String =
        "SELECT DISTINCT conversation_id FROM $tableName"

    override fun getDeleteMessagesSql(): String =
        "DELETE FROM $tableName WHERE conversation_id = ?"
}
