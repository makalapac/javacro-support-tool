package com.trilix.ai.voucher.support_tool.service;

import com.trilix.ai.voucher.support_tool.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@Configuration
public class TransactionTools {

    private static final Logger log = LoggerFactory.getLogger(TransactionTools.class);
    private final TransactionService transactionService;

    public TransactionTools(TransactionService transactionService) {
        this.transactionService = transactionService;
    }



    @Bean
    @Description("Get list of transactions")
    public Function<TransactionRequest, List<TransactionDto>> getTransactions()

    {
        return request -> transactionService.searchTransactions(
                 request.salespoint(),
                 request.saleDate(),
                 request.saleDate(),
                 request.amount(),
                 request.activationCode(),
                 request.isUsed(),
                 request.serviceProvider(),
                 request.voucherName());
    }

    @Bean
    @Description("Cancel voucher transaction")
    public Function<TransactionCancelRequest, Boolean> cancelTransaction() {
        return request -> {
            try {
                transactionService.cancelTransaction(request.activationCode());
                return true;
            } catch (Exception e ) {
                return false;
            }
        };
    }

    @Bean
    @Description("Send email to sales point")
    public Function<SalesPointInfoEmailRequest, Boolean> sendEmailToSalesPoint() {
        return request -> {
            log.info("Sending email to {} with subject {} and content {} ", request.salesPointEmail(), request.subject(), request.message());
            return true;
        };
    }

    @Bean
    @Description("Create ticket in ticketing service")
    public Function<TicketRequest, Boolean> createTicketInTicketingService() {
        return request -> {
            log.info("Creating ticket to support from user {} with subject {} and content {} ", request.userEmail(), request.subject(), request.message());
            return true;
        };
    }

    public record TransactionRequest(
            String salespoint,
            String serviceProvider,
            String activationCode,
            Instant saleDate,
            String voucherName,
            Boolean isUsed,
            BigDecimal amount) {}

    public record TransactionCancelRequest(
            String activationCode){}

    public record TicketRequest(
            String userEmail,
            String subject,
            String message) {}

    public record SalesPointInfoEmailRequest(
            String salesPointEmail,
            String subject,
            String message) {}
}

