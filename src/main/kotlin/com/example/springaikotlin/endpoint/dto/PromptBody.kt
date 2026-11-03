package com.example.springaikotlin.endpoint.dto

import com.example.springaikotlin.application.ConversationType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.ai.chat.prompt.DefaultChatOptions

data class PromptBody(

    @field:NotBlank(message = "NotBlank")
    val conversationId: String? = null,

    // 대화 종류 (GENERAL: 일반 상담, SUPPORT: 고객지원) - 종류별로 다른 테이블에 내역 저장
    @field:NotNull(message = "NotNull")
    val conversationType: ConversationType? = null,

    @field:NotBlank(message = "NotBlank")
    val userPrompt: String? = null,

    val systemPrompt: String? = null,

    val chatOptions: DefaultChatOptions? = null
)
