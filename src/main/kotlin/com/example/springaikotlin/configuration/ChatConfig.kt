package com.example.springaikotlin.configuration

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import com.example.springaikotlin.application.ChatService
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.client.advisor.api.Advisor
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import java.util.Scanner


@Configuration
class ChatConfig {

    @Bean
    fun chatClient(
        chatClientBuilder: ChatClient.Builder,
        advisors: List<Advisor>,
    ): ChatClient {
        return chatClientBuilder.defaultAdvisors(advisors).build()
    }

    @Bean
    fun simpleLoggerAdvisor(): SimpleLoggerAdvisor {
        return SimpleLoggerAdvisor.builder().build()
    }

    // 대화 종류별 메모리 Advisor (Advisor 빈이 아니므로 defaultAdvisors에 포함되지 않고, ChatService가 요청마다 하나를 골라 붙임)
    @Bean
    fun chatMemoryAdvisorRegistry(jdbcTemplate: JdbcTemplate): ChatMemoryAdvisorRegistry {
        return ChatMemoryAdvisorRegistry(
            jdbcTemplate = jdbcTemplate,
            maxMessages = 20,
        )
    }

    // 스프링 부트 서버가 완전히 켜지기 전에 단 한번 자동으로 실행
    @ConditionalOnProperty(prefix = "spring.application", name = ["cli"], havingValue = "true")
    @Bean
    fun cli(
        @Value("\${spring.application.name}") applicationName: String,
        chatService: ChatService,
    ): CommandLineRunner {
        return CommandLineRunner { _ ->
            // 1. 스프링 기본 로그 끄기
            val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
            rootLogger.level = Level.OFF

            // 2. 챗봇 동작
            println("==========================")
            println("[${applicationName} CLI 챗봇을 시작합니다!]")
            println("(종료하려면 'exit' 또는 'quit' 입력)")
            println("==========================")

            Scanner(System.`in`).use { scanner ->
                while (true) {
                    println()

                    print("USER: ")
                    val userMessage: String = scanner.nextLine()

                    // 대화 종료 조건 (무한 루프 탈출)
                    if (userMessage.equals("exit", ignoreCase = true) ||
                        userMessage.equals("quit", ignoreCase = true))
                    {
                        println("대화를 종료합니다. 안녕히 계세요!")
                        break
                    }

                    print("ASSISTANT: ")

                    // 스트리밍 처리 (핵심 변경 포인트)
                    // Flux(스트림)를 toIterable()로 바꾸면 일반적인 for-each 문으로 한 글자씩 꺼내 쓸 수 있음!
                    val chatStream: Iterable<String> = chatService.stream(Prompt(userMessage), "cli", ConversationType.GENERAL).toIterable()

                    for (token in chatStream) {
                        print(token) // 한 글자씩 화면에 출력 (타이핑 효과)
                    }

                    println() // AI 대답이 끝나면 줄바꿈 한 번
                }
            }
        }
    }
}