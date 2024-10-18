package com.trilix.ai.voucher.support_tool.controller;

import com.trilix.ai.voucher.support_tool.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }


    @GetMapping("/spring-ai/embedding-simple")
    public ResponseEntity<?> embedding(@RequestParam(value = "message", defaultValue = "Hello Javacro!") String message) {
        embeddingService.embeddingExampleWithSpringAI(message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/spring-ai/embedding-support-policies")
    public ResponseEntity<?> embeddingSupportPoliciesWithSpringAi(@RequestParam(value = "message", defaultValue = "Hello Javacro!") String message) {
     //   embeddingService.embeddingPoliciesWithSpringAINaive();
     //   embeddingService.embeddingPoliciesWithSpringAIImproved();
        embeddingService.embeddingPoliciesWithSpringAIImprovedWithGPT();
        return ResponseEntity.ok().build();
    }

}
