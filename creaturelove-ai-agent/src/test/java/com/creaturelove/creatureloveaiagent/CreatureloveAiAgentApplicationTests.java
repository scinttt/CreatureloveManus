package com.creaturelove.creatureloveaiagent;

import com.creaturelove.creatureloveaiagent.app.ChatApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class CreatureloveAiAgentApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource
    private ChatApp chatApp;

    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();
        // first round
        String message = "Hello, I am Creaturelove";
        String answer = chatApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // Second Round
        message = "I want to let my partner(Fey) love me more";
        answer = chatApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // Third Round
        message = "What's the name of my partner? I just told you. Help me to remember it";
        answer = chatApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);

    }

}
