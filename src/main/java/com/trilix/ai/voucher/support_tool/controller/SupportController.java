package com.trilix.ai.voucher.support_tool.controller;

import com.trilix.ai.voucher.support_tool.dto.TransactionDto;
import com.trilix.ai.voucher.support_tool.entity.Salespoint;
import com.trilix.ai.voucher.support_tool.entity.Transaction;
import com.trilix.ai.voucher.support_tool.entity.Voucher;
import com.trilix.ai.voucher.support_tool.service.SupportService;
import com.trilix.ai.voucher.support_tool.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stringtemplate.v4.ST;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Tag(name = "Support Tool", description = "The magnificient support tool for the AI voucher system")
@RestController
public class SupportController {

    private final SupportService supportService;
    private final TransactionService transactionService;

    public SupportController(SupportService supportService, TransactionService transactionService) {
        this.supportService = supportService;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transaction logs for sold vouchers")
    @GetMapping("/transactions")
    public List<TransactionDto> getTransactions() {
        return supportService.getAllTransactions();
    }

    @Operation(summary = "Get all transactions filtered", description = "Retrieve a list of all transaction logs for sold vouchers filtered by sales point name, date range, amount, or activation code")
    @GetMapping("/transactions/search")
    public List<TransactionDto> searchTransactions(
            @RequestParam(required = false) String salesPointName,
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) BigDecimal amount,
            @RequestParam(required = false) String activationCode) {
        return transactionService.searchTransactions(salesPointName, startDate, endDate, amount, activationCode, null, null, null);
    }

    @Operation(summary = "Get all vouchers", description = "Retrieve a list of all available vouchers")
    @GetMapping("/vouchers")
    public List<Voucher> getVouchers() {
        return supportService.getAllVouchers();
    }

    @Operation(summary = "Get all sales points", description = "Retrieve a list of all sales points")
    @GetMapping("/sales-points")
    public List<Salespoint> getSalesPoints() {
        return supportService.getAllSalesPoints();
    }

    @Operation(summary = "Chat with Voucher company TM support agent", description = "Opens a chat communication with a support agent")
    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return supportService.chat(query);
    }

}
