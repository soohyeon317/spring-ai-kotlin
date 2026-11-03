package com.example.springaikotlin.application

import com.example.springaikotlin.configuration.ChatMemoryAdvisorRegistry
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val chatMemoryAdvisorRegistry: ChatMemoryAdvisorRegistry,
) {

    fun call(
        prompt: Prompt,
        conversationId: String,
        conversationType: ConversationType,
    ): ChatResponse {
        return prepareRequest(
            prompt = prompt,
            conversationId = conversationId,
            conversationType = conversationType,
        ).call().chatResponse()!!
    }

    fun stream(
        prompt: Prompt,
        conversationId: String,
        conversationType: ConversationType,
    ): Flux<String> {
        return prepareRequest(
            prompt = prompt,
            conversationId = conversationId,
            conversationType = conversationType,
        ).stream().content()
    }

    private fun prepareRequest(
        prompt: Prompt,
        conversationId: String,
        conversationType: ConversationType,
    ): ChatClient.ChatClientRequestSpec {
        return chatClient.prompt(prompt)
            .advisors { advisorSpec ->
                // 대화 종류에 해당하는 메모리 Advisor만 이 요청에 붙여서, 해당 테이블에만 저장/조회되도록 함
                advisorSpec.advisors(chatMemoryAdvisorRegistry.get(conversationType))
                advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)
            }
    }
}