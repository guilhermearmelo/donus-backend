package com.donus.backend.service;

import com.donus.backend.domain.Account;
import com.donus.backend.domain.Costumer;
import com.donus.backend.repository.AccountRepository;
import com.donus.backend.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CostumerRepository costumerRepository;

    public boolean validateUserAccountByCode(String code, String userKey){
        Account account = accountRepository.findByCode(code);
        Costumer costumer = costumerRepository.findById(account.getCostumer().getId());
        return Objects.equals(costumer.getPassword(), userKey);
    }

    public boolean validateUserAccount(String accountId, String userKey){
        Account account = accountRepository.findByCode(accountId);
        Costumer costumer = costumerRepository.findById(account.getCostumer().getId());
        return Objects.equals(costumer.getPassword(), userKey);
    }

    public boolean validateUserPassword(String password, long userId){
        Costumer costumer = costumerRepository.findById(userId);
        return Objects.equals(costumer.getPassword(), new BCryptPasswordEncoder().encode(password));
    }

}
