package com.example.springaikotlin.application

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ChatService(
    private val chatClient: ChatClient
) {

    fun call(
        prompt: Prompt,
        conversationId: String
    ): ChatResponse {
        return prepareRequest(
            prompt = prompt,
            conversationId = conversationId
        ).call().chatResponse()!!
    }

    fun stream(
        prompt: Prompt,
        conversationId: String
    ): Flux<String> {
        return prepareRequest(
            prompt = prompt,
            conversationId = conversationId
        ).stream().content()
    }

    private fun prepareRequest(
        prompt: Prompt,
        conversationId: String,
    ): ChatClient.ChatClientRequestSpec {
        return chatClient.prompt(prompt)
            .advisors { advisorSpec ->
                advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)
            }
    }
}