package com.trilix.ai.voucher.support_tool.repository;

import com.trilix.ai.voucher.support_tool.entity.Salespoint;
import com.trilix.ai.voucher.support_tool.entity.Transaction;
import com.trilix.ai.voucher.support_tool.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByActivationCode(String activationCode);

    Optional<List<Transaction>> findBySalespoint(Salespoint salesPoint);

    Optional<List<Transaction>> findByVoucher(Voucher voucher);
}
