package com.trilix.ai.voucher.support_tool.service;

import com.trilix.ai.voucher.support_tool.dto.TransactionDto;
import com.trilix.ai.voucher.support_tool.entity.Transaction;
import com.trilix.ai.voucher.support_tool.entity.Voucher;
import com.trilix.ai.voucher.support_tool.entity.Salespoint;
import com.trilix.ai.voucher.support_tool.repository.SalesPointRepository;
import com.trilix.ai.voucher.support_tool.repository.TransactionRepository;
import com.trilix.ai.voucher.support_tool.repository.VoucherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportService {
    private final SalesPointRepository salesPointRepository;
    private final VoucherRepository voucherRepository;
    private final TransactionRepository transactionRepository;
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    @Value("classpath:support_policies.html")
    Resource termsOfServiceDocs;

    private static final Logger logger = LoggerFactory.getLogger(SupportService.class);

    public SupportService(SalesPointRepository salesPointRepository, VoucherRepository voucherRepository, TransactionRepository transactionRepository, ChatClient.Builder chatClientBuilder, VectorStore vectorStore, ChatMemory chatMemory) {
        this.salesPointRepository = salesPointRepository;
        this.voucherRepository = voucherRepository;
        this.transactionRepository = transactionRepository;
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    public List<Voucher> getAllVouchers() {
        logger.info("Getting all vouchers");
        return voucherRepository.findAll();
    }

    public List<Salespoint> getAllSalesPoints() {
        logger.info("Getting all sales points");
        return salesPointRepository.findAll();
    }

    public List<TransactionDto> getAllTransactions() {
        logger.info("Getting all transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    
    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();

        // Set transaction fields
        dto.setId(transaction.getId());
        dto.setSaleDate(transaction.getSaleDate());
        dto.setAmount(transaction.getAmount());
        dto.setIsUsed(transaction.getUsed());
        dto.setUsedDate(transaction.getUsedDate());
        dto.setActivationCode(transaction.getActivationCode());

        // Set voucher fields
        if (transaction.getVoucher() != null) {
            dto.setVoucherName(transaction.getVoucher().getName());
            dto.setServiceProvider(transaction.getVoucher().getServiceProvider());
        }

        // Set sales point fields if they are part of the transaction
        if (transaction.getSalespoint() != null) {
            dto.setSalesPointName(transaction.getSalespoint().getName());
            dto.setSalesPointLocation(transaction.getSalespoint().getLocation());
        }

        dto.setStatus(transaction.getStatus());

        return dto;
    }

    public String chat(String query) {
        String SYSTEM_MESSAGE = """
                You are a support agent for the voucher company TM. Your task is to answer the user's query accurately and politely.
                
                Use the provided context: Leverage the available context to respond to the user. If you need more information, ask the user directly for clarification.
                
                Escalation: If you're unable to resolve the issue, politely inform the user that you will escalate the query
                 to a more experienced support agent. Ask for the user's email address and create
                a new ticket summarizing the issue and your conversation using the createTicketInTicketingService function.
                Ensure you only create the ticket after receiving the email address.
                
                Voucher inquiries:
                
                If the user asks about their vouchers, request the activation code and attempt to retrieve the relevant voucher using getTransactions.
                If the user wishes to cancel a voucher, request the activation code, verify if cancellation is allowed based on the voucher's terms of service, and communicate whether cancellation is possible.
                If cancellation is permitted, request user confirmation. Upon receiving confirmation, cancel the voucher and notify the salespoint via email.
                If cancellation is not possible, explain the reasons to the user.
                End of conversation: When the conversation is complete, say goodbye and end with a Michael Scott quote (unless you are waiting for further input from the user).
                """;

        ChatResponse chatResponse = chatClient.prompt()
                .system(SYSTEM_MESSAGE)
                .advisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()),
                        new LoggingAdvisor()
                )
                .functions("cancelTransaction", "getTransactions", "createTicketInTicketingService", "sendEmailToSalesPoint")
                .user(query)
                .call()
                .chatResponse();
        logger.info("Chat response: {}", chatResponse);

        return chatResponse.getResult().getOutput().getContent();
    }
}
