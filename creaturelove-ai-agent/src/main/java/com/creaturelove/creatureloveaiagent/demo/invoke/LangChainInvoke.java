package com.creaturelove.creatureloveaiagent.demo.invoke;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LangChainInvoke {

    public static void main(String[] args) {
        String model = "gpt-3.5-turbo";

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey("")
                .modelName(model)
                .build();

        String answer = chatModel.chat("What is the capital of France?");
        System.out.println("Answer: " + answer);

    }
}
