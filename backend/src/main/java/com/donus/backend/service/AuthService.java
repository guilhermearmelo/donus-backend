package com.donus.backend.service;

import com.donus.backend.domain.Account;
import com.donus.backend.domain.User;
import com.donus.backend.repository.AccountRepository;
import com.donus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean validateUserAccount(String accountId, String userKey){
        Account account = accountRepository.findByCode(accountId);
        User user = userRepository.findById(account.getUser().getId());
        return Objects.equals(user.getPassword(), userKey);
    }

    public boolean validateUserPassword(String password, long userId){
        User user = userRepository.findById(userId);
        return Objects.equals(user.getPassword(), password);
    }





}
