package com.trilix.ai.voucher.support_tool.repository;

import com.trilix.ai.voucher.support_tool.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByName(String voucherName);
}
