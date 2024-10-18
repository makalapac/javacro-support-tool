package com.trilix.ai.voucher.support_tool.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAIChatCompletion {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIChatCompletion.class);

    public static void main(String[] args) {
        String apiKey = "GO TO https://platform.openai.com/account/api-keys TO GET YOUR API KEY";
        String endpoint = "https://api.openai.com/v1/chat/completions";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(createJsonBody()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            logger.error("An error occurred while sending the request", e);
        }
    }

    private static String createJsonBody() {
        return """
                {
                    "model": "gpt-4o",
                    "messages": [
                        {"role": "user", "content": "Hey, say hello to audience at my presentation at Javacro conference! But sound like some British punk band "}
                    ]
                }
                """;
    }
}

