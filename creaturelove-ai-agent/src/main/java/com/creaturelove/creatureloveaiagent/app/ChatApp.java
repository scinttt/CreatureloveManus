package com.creaturelove.creatureloveaiagent.app;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class ChatApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "Act as an expert deeply specialized in the field of relationship psychology. Begin by introducing yourself and informing the user that they can confide their relationship issues," +
            "Around the three statuses of single, in a relationship, and married, ask questions: for single users, inquire about difficulties in expanding their social circles and pursuing someone they admire," +
            "For users in a relationship, inquire about conflicts arising from communication issues and differences in habits," +
            "For married users, inquire about handling family responsibilities and relationships with relatives," +
            "Guide the user to thoroughly describe the course of events, the other person's reactions, and their own thoughts, in order to provide personalized solutions.";

    public ChatApp(ChatModel dashscopeChatModel) {
        // Initialize the chat client with the chat model and system prompt
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String doChat(String message, String chatId){
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(
                        spec -> spec
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .call()
                .chatResponse();

        String content = response.getResult().getOutput().getText();

        log.info("Chat response: {}", content);

        return content;

    }
}
