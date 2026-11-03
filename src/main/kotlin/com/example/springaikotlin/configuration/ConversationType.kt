package com.example.springaikotlin.configuration

/**
 * 대화 종류. 종류마다 대화 내역을 별도 테이블에 저장한다.
 * 테이블을 추가/변경할 경우 schema/conversation-history-postgresql.sql 도 함께 수정해야 한다.
 */
enum class ConversationType(val tableName: String) {
    GENERAL("general_conversation_history"), // 일반 상담
    SUPPORT("support_conversation_history"), // 고객지원
}
