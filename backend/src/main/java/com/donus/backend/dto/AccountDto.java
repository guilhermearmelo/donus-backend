package com.donus.backend.dto;

import com.donus.backend.domain.Account;
import lombok.Data;

@Data
public class AccountDto {

    private String code;
    private Double balance;

    public AccountDto(Account account){
        this.code = account.getCode();
        this.balance = account.getBalance();
    }
}
