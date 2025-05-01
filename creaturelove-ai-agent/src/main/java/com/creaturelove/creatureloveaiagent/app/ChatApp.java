package com.creaturelove.creatureloveaiagent.app;


import com.creaturelove.creatureloveaiagent.advisor.MyLoggerAdvisor;
import com.creaturelove.creatureloveaiagent.chatmemory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class ChatApp {
    private final ChatClient chatClient;
    private final JdbcChatMemory jdbcChatMemory;

    private static final String SYSTEM_PROMPT = "Act as an expert deeply specialized in the field of relationship psychology. Begin by introducing yourself and informing the user that they can confide their relationship issues," +
            "Around the three statuses of single, in a relationship, and married, ask questions: for single users, inquire about difficulties in expanding their social circles and pursuing someone they admire," +
            "For users in a relationship, inquire about conflicts arising from communication issues and differences in habits," +
            "For married users, inquire about handling family responsibilities and relationships with relatives," +
            "Guide the user to thoroughly describe the course of events, the other person's reactions, and their own thoughts, in order to provide personalized solutions.";

    public ChatApp(ChatModel dashscopeChatModel, @Qualifier("bannedWords") List<String> bannedWords, JdbcChatMemory jdbcChatMemory) {
        this.jdbcChatMemory = jdbcChatMemory;
        // Initialize the chat client with the chat model and system prompt
        String fileDir = System.getProperty("user.dir") + "/char-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor(),
                        new SafeGuardAdvisor(bannedWords)
                )
                .build();
    }

    public String doChat(String message, String chatId){
        jdbcChatMemory.add(chatId, List.of(new UserMessage(message)));
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(
                        spec -> spec
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .call()
                .chatResponse();

        jdbcChatMemory.add(chatId, List.of(response.getResult().getOutput()));

        String content = response.getResult().getOutput().getText();

        log.info("Chat response: {}", content);

        return content;

    }

    public ChatReport doChatWithReport(String message, String chatId){
        ChatReport report = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "Generate chat report after every round of conversation. The title is {username}'s ChatReport, content is suggestion list")
                .user(message)
                .advisors(
                        spec -> spec
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10)
                )
                .call()
                .entity(ChatReport.class);
        log.info("Chat report: {}", report);
        return report;
    }
}

