package com.trilix.ai.voucher.support_tool.controller;

import com.trilix.ai.voucher.support_tool.service.EmbeddingService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@RestController
@Tag(name = "Examples", description = "Examples of using the AI stuff")
public class ExamplesController {

    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;

    private static final Logger logger = LoggerFactory.getLogger(ExamplesController.class);

    public ExamplesController(ChatClient.Builder chatClientBuilder, EmbeddingService embeddingService) {
        this.chatClient = chatClientBuilder.build();
        this.embeddingService = embeddingService;
    }

    @Operation(summary = "Spring AI basic example", description = "A basic example")
    @GetMapping("/spring-ai/basic-example")
    public String springAIBasicExample() {
        ChatResponse chatResponse = chatClient.prompt()
                .user("Hey, say hello to audience at my presentation at Javacro conference! ")
                .system("Always sound like a british punk band")
                .call()
                .chatResponse();

        logger.info("Chat response: {}", chatResponse);

        return chatResponse.getResults().getFirst().getOutput().getContent();
    }

    @Operation(summary = "LangChain basic example", description = "A basic example")
    @GetMapping("/langchain/basic-example")
    public String langChainBasicExample(){

        //This could be a bean in Spring
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .build();

        //This could be a bean in Spring
        Assistant assistant = AiServices.create(Assistant.class, model);

        return assistant.chat("Hey, say hello to audience at my presentation at Javacro conference!");

    }

    interface Assistant {
        @SystemMessage("Always sound like a british punk band")
        String chat(String userMessage);
    }

    @Operation(summary = "Embedding example", description = "A basic example")
    @GetMapping("/spring-ai/basic-example-embedding")
    public void embeddingExampleWithSpringAI() {
        embeddingService.embeddingExampleWithSpringAI("Hello Javacro!");
    }




}
