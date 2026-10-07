package com.smartbook.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long accountId;
    private String email;
    private BigDecimal balance;

    public AccountResponse(
            Long accountId,
            String email,
            BigDecimal balance) {

        this.accountId = accountId;
        this.email = email;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}