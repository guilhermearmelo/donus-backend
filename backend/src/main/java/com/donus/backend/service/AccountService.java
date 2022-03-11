package com.donus.backend.service;

import com.donus.backend.domain.Account;
import com.donus.backend.domain.User;
import com.donus.backend.dto.*;
import com.donus.backend.repository.AccountRepository;
import com.donus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public AccountDto insert(AccountInsertionDto accountInsertionDto) {
        Account account = this.parseDtoToEntity(accountInsertionDto);

        return parseEntityToDto(accountRepository.save(account));
    }

    public void remove(long id) {
        Account account = accountRepository.findById(id);
        accountRepository.delete(account);
    }

    public List<AccountDto> findAll() {
        List<Account> accountList;
        List<AccountDto> accountDtoList = new ArrayList<>();

        accountList = accountRepository.findAll();
        for(Account account: accountList) {
            AccountDto accountDto = this.parseEntityToDto(account);
            accountDtoList.add(accountDto);
        }

        return accountDtoList;
    }

    public AccountDto findById(long id) {
        Account account = accountRepository.findById(id);

        return parseEntityToDto(account);
    }

    public AccountDto doTransaction(TransactionDto transactionDto) {
        Account source = accountRepository.findByCode(transactionDto.getSourceAccount());
        Account target = accountRepository.findByCode(transactionDto.getTargetAccount());
        Double amount = transactionDto.getAmount();
        AccountDto accountDto = new AccountDto(source);

        if(!source.hasMoney(amount)) return accountDto;
        if(amount <= 0) return accountDto;

        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);

        accountRepository.save(source);
        accountRepository.save(target);

        return new AccountDto(source);
    }

    public AccountDto makeDeposit(DepositDto depositDto) {
        Account target = accountRepository.findByCode(depositDto.getTargetAccount());
        Double amount = depositDto.getAmount();

        if(!((0<amount)&&(amount<=2000))) return new AccountDto(target);

        target.setBalance(target.getBalance() + amount);

        accountRepository.save(target);

        return new AccountDto(target);
    }

    public Account parseDtoToEntity(AccountDto accountDto) {
        Account account = new Account(accountDto.getCode(), accountDto.getBalance());

        return account;
    }

    public Account parseDtoToEntity(AccountInsertionDto accountInsertionDto) {
        Account account = new Account(accountInsertionDto.getAccountId(), accountInsertionDto.getBalance());
        User user = userRepository.findById((long)accountInsertionDto.getUserId());
        account.setUser(user);

        return account;
    }

    public AccountDto parseEntityToDto(Account account){
        AccountDto accountDto = new AccountDto(account);

        return accountDto;
    }

}
