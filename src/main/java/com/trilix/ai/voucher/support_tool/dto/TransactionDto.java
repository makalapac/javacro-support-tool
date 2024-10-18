package com.trilix.ai.voucher.support_tool.dto;

import com.trilix.ai.voucher.support_tool.entity.Status;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDto {
    private Integer id;
    private Instant saleDate;
    private BigDecimal amount;
    private Boolean isUsed;
    private Instant usedDate;
    private String voucherName;
    private String serviceProvider;
    private String salesPointName;
    private String salesPointLocation;
    private String activationCode;
    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Instant getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(Instant usedDate) {
        this.usedDate = usedDate;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getSalesPointName() {
        return salesPointName;
    }

    public void setSalesPointName(String salesPointName) {
        this.salesPointName = salesPointName;
    }

    public String getSalesPointLocation() {
        return salesPointLocation;
    }

    public void setSalesPointLocation(String salesPointLocation) {
        this.salesPointLocation = salesPointLocation;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
