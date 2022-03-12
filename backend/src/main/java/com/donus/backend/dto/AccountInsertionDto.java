package com.donus.backend.dto;

import com.donus.backend.domain.Account;
import lombok.Data;

@Data
public class AccountInsertionDto {
    private String accountId;
    private Double balance;
    private Long userId;
    private String password;

    public AccountInsertionDto(){}

    public AccountInsertionDto(Account account, Long id, String password){
        this.accountId = account.getCode();
        this.balance = account.getBalance();
        this.userId = id;
        this.password = password;
    }
}
