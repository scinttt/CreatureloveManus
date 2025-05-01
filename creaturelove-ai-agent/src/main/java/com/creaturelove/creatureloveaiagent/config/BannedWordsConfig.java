package com.creaturelove.creatureloveaiagent.config;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class BannedWordsConfig {

    @Bean("bannedWords")
    public List<String> bannedWords(
            @Value("classpath:bad-words.txt") Resource resource
    ) throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
        ) {
            return reader.lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }catch (IOException e) {
            log.warn("违禁词文件加载失败，返回空列表", e);
            return List.of();
        }
    }
}