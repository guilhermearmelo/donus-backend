package com.donus.backend.dto;

import lombok.Data;

@Data
public class DepositDto {

    private String targetAccount;
    private Double amount;
}
