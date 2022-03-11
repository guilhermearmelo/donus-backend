package com.donus.backend.dto;

import com.donus.backend.domain.Account;
import lombok.Data;

@Data
public class AccountInsertionDto {
    private String accountId;
    private Double balance;
    private Long userId;

    public AccountInsertionDto(){}

    public AccountInsertionDto(Account account, Long id){
        this.accountId = account.getCode();
        this.balance = account.getBalance();
        this.userId = id;
    }
}
