package com.trilix.ai.voucher.support_tool.entity;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionSpecification {

    public static Specification<Transaction> bySalesPointName(String salesPointName) {
        return (root, query, criteriaBuilder) -> {
            if (salesPointName == null || salesPointName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("salespoint").get("name"), salesPointName);
        };
    }

    public static Specification<Transaction> bySaleDateRange(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("saleDate"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("saleDate"), startDate);
            } else if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("saleDate"), endDate);
            }
            return null;
        };
    }

    public static Specification<Transaction> byAmount(BigDecimal amount) {
        return (root, query, criteriaBuilder) -> {
            if (amount == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("amount"), amount);
        };
    }

    public static Specification<Transaction> byActivationCode(String activationCode) {
        return (root, query, criteriaBuilder) -> {
            if (activationCode == null || activationCode.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("activationCode"), activationCode);
        };
    }

    public static Specification<Transaction> byUsed(Boolean isUsed) {
        return (root, query, criteriaBuilder) -> {
            if (isUsed == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("used"), isUsed);
        };
    }

    public static Specification<Transaction> byServiceProvider(String serviceProvider) {
        return (root, query, criteriaBuilder) -> {
            if (serviceProvider == null || serviceProvider.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("serviceProvider"), serviceProvider);
        };
    }

    public static Specification<Transaction> byVoucherName(String voucherName) {
        return (root, query, criteriaBuilder) -> {
            if (voucherName == null || voucherName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("voucher").get("name"), voucherName);
        };
    }


}
