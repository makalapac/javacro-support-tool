package com.trilix.ai.voucher.support_tool.service;

import com.trilix.ai.voucher.support_tool.dto.TransactionDto;
import com.trilix.ai.voucher.support_tool.entity.Status;
import com.trilix.ai.voucher.support_tool.entity.Transaction;
import com.trilix.ai.voucher.support_tool.entity.TransactionSpecification;
import com.trilix.ai.voucher.support_tool.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionDto> searchTransactions(String salesPointName, Instant startDate, Instant endDate, BigDecimal amount, String activationCode, Boolean isUsed, String serviceProvider, String voucherName) {
        Specification<Transaction> spec = Specification
                .where(TransactionSpecification.bySalesPointName(salesPointName))
                .and(TransactionSpecification.bySaleDateRange(startDate, endDate))
                .and(TransactionSpecification.byAmount(amount))
                .and(TransactionSpecification.byActivationCode(activationCode))
                .and(TransactionSpecification.byUsed(isUsed))
                .and(TransactionSpecification.byServiceProvider(serviceProvider))
                .and(TransactionSpecification.byVoucherName(voucherName));

        log.info("Searching transactions with criteria: {}", spec.toString());
        List<Transaction> transactions = transactionRepository.findAll(spec);
        log.info("Found {} transactions", transactions.size());
        transactions.forEach(transaction -> log.info("Transaction: {}", transaction));
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public void cancelTransaction(String activationCode) {
        log.info("Cancelling transaction with activation code: {}", activationCode);
        Transaction transaction = transactionRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid activation code"));

        if (transaction.getUsed()) {
            throw new IllegalStateException("Voucher is already used");
        }

        transaction.setStatus(Status.CANCELED);

        transactionRepository.save(transaction);
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
        dto.setStatus(transaction.getStatus());

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

        return dto;
    }

}
