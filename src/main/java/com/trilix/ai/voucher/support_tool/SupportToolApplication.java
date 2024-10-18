package com.trilix.ai.voucher.support_tool;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SupportToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupportToolApplication.class, args);
	}

	@Bean
	public ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}

}
