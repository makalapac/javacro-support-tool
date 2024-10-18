package com.trilix.ai.voucher.support_tool.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilix.ai.voucher.support_tool.entity.TermsChunk;
import com.trilix.ai.voucher.support_tool.entity.TermsMetadata;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    private static final Logger logger = LoggerFactory.getLogger(SupportService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("classpath:support_policies.html")
    Resource termsOfServiceDocs;

    @Value("classpath:voucher_policy_chunks.json")
    Resource termsOfServiceJSON;

    public EmbeddingService(EmbeddingModel embeddingModel, VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public Map<String, EmbeddingResponse> embeddingExampleWithSpringAI(String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    public void embeddingPoliciesWithSpringAINaive() {


        var doc = new TikaDocumentReader(termsOfServiceDocs).read();
        var chunks = new TokenTextSplitter().transform(doc);
        vectorStore.write(chunks);

    }

    public void embeddingPoliciesWithSpringAIImproved() {

        try {
            var chunks = splitHtmlIntoChunks(StreamUtils.copyToString(termsOfServiceDocs.getInputStream(), StandardCharsets.UTF_8));
            chunks.forEach(chunk -> logger.info("Splited: {}", chunk));
            chunks.forEach(chunk -> vectorStore.write(new TikaDocumentReader(chunk).read()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading HTML file", e);
        }

    }

    public void embeddingPoliciesWithSpringAIImprovedWithGPT() {
        logger.info("Embedding policies with SpringAI improved with GPT");

        try {

            String SYSTEM_MESSAGE = """
                    Bellow you can find a document that I will use for creating application for demonstration of RAG with SpringAI.
                
                    Split this document into chunks that are semantically good for embedding into vector database.
                    
                    Return it as a JSON that contains text and metadata and just JSON without any additional text or formatting.
                    
                    """;

/*            List<TermsChunk> chunks = chatClient.prompt()
                    .system(SYSTEM_MESSAGE)
                    .user(termsOfServiceDocs)
                    .call()
                    .entity(new ParameterizedTypeReference<List<TermsChunk>>() {});*/

            //THIS IS OPTION WITHOUT CHAT CLIENT ENTITY
            //List<TermsChunk> chunks = convertGPTResponseToTermsChunk(String.valueOf(chatResponse.getResult().getOutput().getContent()));

            List<Document> documents = new ArrayList<>();

            //THIS IS EMERGENCY OFFLINE GPT SIMULATION OPTION FOR PRESENTATION THAT USES STORED JSON
            List<TermsChunk> chunks = objectMapper.readValue(termsOfServiceJSON.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, TermsChunk.class));

            chunks.forEach(chunk -> documents.add(convertTermsChunkToDocument(chunk)));

            vectorStore.add(documents);


            var testQueryResults = vectorStore.similaritySearch("User wants to cancel LuxVouchers. What to do?");
            testQueryResults.forEach(result -> logger.info("Result: {}", result));


        } catch (Exception e) {
            throw new RuntimeException("Error with embedding", e);
        }

    }

    private Document convertTermsChunkToDocument(TermsChunk chunk) {
        Map<String, Object> metadataMap = new HashMap<>();
        metadataMap.put("Section", Optional.ofNullable(chunk.metadata().section()).orElse(""));
        metadataMap.put("Subsection", Optional.ofNullable(chunk.metadata().subsection()).orElse(""));
        return new Document(chunk.text(), metadataMap);
    }

    public List<String> splitHtmlIntoChunks(String htmlContent) {
        List<String> chunks = new ArrayList<>();

        // Parse the HTML content using Jsoup
        var doc = Jsoup.parse(htmlContent);

        // Extract semantic sections like headings and paragraphs
        Elements elements = doc.select("h1, h2, h3, h4, h5, h6, p, ol, ul");

        StringBuilder currentChunk = new StringBuilder();
        for (Element element : elements) {
            // Check if we encounter a heading (semantic split point)
            if (element.tagName().matches("h[1-6]")) {
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString());
                    currentChunk.setLength(0); // Clear the current chunk
                }
            }
            currentChunk.append(element.text()).append("\n");
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString());
        }

        return chunks;
    }

    public static List<TermsChunk> convertGPTResponseToTermsChunk(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, TermsChunk.class));
    }

    //Unrelated for demo
    private void playingAroundWithVectoreStoreForTest() {
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("GLjive su fine za jesti", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Riba je zdrava", Map.of("Section", "hrana", "Subsection", "riba")));
        documents.add(new Document("Blitva je super prilog za ribu", Map.of("Section", "hrana", "Subsection", "riba")));
        documents.add(new Document("Gljive treba jesti samo ako se znaju od gdje su ubrane", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Postoje otrovne i neotrovne gljive", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Gljive se uglavnom beri u jesen", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Šampinjoni su jedna od najpularnijih gljiva", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Vrganj je top!", Map.of("Section", "hrana", "Subsection", "gljive")));
        documents.add(new Document("Ryzen je top procesor za dobru cijenu", Map.of("Section", "PC", "Subsection", "Procesori")));
        
        vectorStore.add(documents);

        var testQueryResults = vectorStore.similaritySearch("nešto o gljivama?");
        testQueryResults.forEach(result -> logger.info("Result: {}", result));
    }

}
