package com.example.springaikotlin.endpoint.dto

import jakarta.validation.constraints.NotBlank
import org.springframework.ai.chat.prompt.DefaultChatOptions

data class PromptBody(

    @field:NotBlank(message = "NotBlank")
    val conversationId: String? = null,

    @field:NotBlank(message = "NotBlank")
    val userPrompt: String? = null,

    val systemPrompt: String? = null,

    val chatOptions: DefaultChatOptions? = null
)
