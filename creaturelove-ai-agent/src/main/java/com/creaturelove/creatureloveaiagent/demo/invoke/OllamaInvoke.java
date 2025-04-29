package com.creaturelove.creatureloveaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OllamaInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
//        AssistantMessage output = ollamaChatModel.call(new Prompt("Hello, I am Creaturelove"))
//                .getResult()
//                .getOutput();
//        System.out.println("Output: " + output.getText());
//
//        ChatResponse response = ollamaChatModel.call(new Prompt("Hello"));

        ChatClient chatClient = ChatClient.builder(ollamaChatModel)
                .defaultSystem("Hello, You are a love consultant")
                .build();

        String response = chatClient.prompt()
                .user("Hello")
                .call()
                .content();
    }
}
