package com.creaturelove.creatureloveaiagent.config;

import org.springframework.ai.chat.memory.jdbc.JdbcChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatMemoryConfig {

    @Bean
    public JdbcChatMemory jdbcChatMemory(JdbcTemplate jdbcTemplate) {
        return new JdbcChatMemory(JdbcChatMemoryConfig.builder()
                .jdbcTemplate(jdbcTemplate)
                .build());
    }
}
