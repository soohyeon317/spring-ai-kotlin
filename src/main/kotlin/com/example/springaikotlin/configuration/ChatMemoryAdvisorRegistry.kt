package com.example.springaikotlin.configuration

import com.example.springaikotlin.application.ConversationType
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.jdbc.core.JdbcTemplate

/**
 * 대화 종류별 메모리 Advisor 묶음.
 *
 * 메모리 Advisor를 각각 Advisor 빈으로 등록하면 ChatClient의 defaultAdvisors에 전부 들어가
 * 모든 요청이 모든 테이블에 저장되므로, 빈으로 등록하지 않고 여기서 보관한 뒤 요청마다 하나만 골라 붙인다.
 * (Repository/ChatMemory도 빈으로 여러 개 등록하면 Spring AI 자동 구성의 ChatMemory 생성 시 주입 대상이 모호해지므로 내부에서만 생성)
 */
class ChatMemoryAdvisorRegistry(
    jdbcTemplate: JdbcTemplate,
    maxMessages: Int,
) {

    private val advisors: Map<ConversationType, MessageChatMemoryAdvisor> =
        ConversationType.entries.associateWith { type ->
            val repository = JdbcChatMemoryRepository.builder()
                .jdbcTemplate(jdbcTemplate)
                .dialect(ConversationHistoryDialect(type.tableName))
                .build()

            val chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(maxMessages)
                .build()

            MessageChatMemoryAdvisor.builder(chatMemory).build()
        }

    fun get(type: ConversationType): MessageChatMemoryAdvisor = advisors.getValue(type)
}
