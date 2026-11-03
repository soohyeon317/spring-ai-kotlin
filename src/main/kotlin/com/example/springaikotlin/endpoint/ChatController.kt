package com.example.springaikotlin.endpoint

import com.example.springaikotlin.application.ChatService
import com.example.springaikotlin.endpoint.dto.PromptBody
import jakarta.validation.Valid
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping(value = ["/call"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun call(
        @RequestBody @Valid promptBody: PromptBody
    ): ChatResponse {
        val prompt = createPrompt(promptBody = promptBody)

        return this.chatService.call(
            prompt = prompt,
            conversationId = promptBody.conversationId!!,
            conversationType = promptBody.conversationType!!,
        )
    }

    @PostMapping(value = ["/stream"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun stream(
        @RequestBody @Valid promptBody: PromptBody
    ): Flux<String> {
        val prompt = createPrompt(promptBody = promptBody)

        return this.chatService.stream(
            prompt = prompt,
            conversationId = promptBody.conversationId!!,
            conversationType = promptBody.conversationType!!,
        )
    }

    companion object {

        private fun createPrompt(promptBody: PromptBody): Prompt {
            // 1. 메시지들을 차곡차곡 담을 빈 리스트 생성
            val messages = mutableListOf<Message>()

            // 2. systemPrompt가 입력됐다면 리스트에 넣기
            if (!promptBody.systemPrompt.isNullOrBlank()) {
                messages.add(
                    SystemMessage(promptBody.systemPrompt)
                )
            }

            // 3. userPrompt는 필수 값이므로 무조건 리스트에 넣기
            messages.add(
                UserMessage(promptBody.userPrompt!!)
            )

            // 4. 리스트에 담긴 메시지들로 프롬프트 조립하기
            val promptBuilder = Prompt.builder().messages(messages)

            // 5. chatOptions가 입력됐다면 적용하기
            if (promptBody.chatOptions != null) {
                promptBuilder.chatOptions(promptBody.chatOptions)
            }

            return promptBuilder.build()
        }
    }
}