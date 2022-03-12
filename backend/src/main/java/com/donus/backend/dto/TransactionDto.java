package com.donus.backend.dto;

import lombok.Data;

@Data
public class TransactionDto {

    private String sourceAccount;
    private String targetAccount;
    private String userKey;
    private Double amount;
}
