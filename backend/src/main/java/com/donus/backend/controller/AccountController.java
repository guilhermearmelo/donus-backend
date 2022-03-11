package com.donus.backend.controller;

import com.donus.backend.dto.*;
import com.donus.backend.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api")
@CrossOrigin(origins="*")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/account")
    @ApiOperation(value = "Creates an account for a given user Id")
    public AccountDto insertAccount(@RequestBody AccountInsertionDto accountInsertionDto) {
        return accountService.insert(accountInsertionDto);
    }

    @DeleteMapping("/account/{id}")
    @ApiOperation(value = "Removes an account given an id")
    public void deleteAccountById(@PathVariable(value="id") long id) {
        accountService.remove(id);
    }

    @GetMapping("/accounts")
    @ApiOperation(value = "Returns a list of all accounts")
    public List<AccountDto> listUsers(){
        return accountService.findAll();
    }

    @GetMapping("/account/{id}")
    @ApiOperation(value = "Returns one account given an id")
    public AccountDto findAccountById(@PathVariable(value="id") long id) {
        return accountService.findById(id);
    }

    @PutMapping("/transaction")
    @ApiOperation(value = "Makes balance transaction between two accounts")
    public AccountDto transactionBetweenAccounts(@RequestBody TransactionDto transactionDto) {
        return accountService.doTransaction(transactionDto);
    }

    @PutMapping("/deposit")
    @ApiOperation(value = "Deposit a certain amount in a given account")
    public AccountDto depositInAccount(@RequestBody DepositDto depositDto) {
        return accountService.makeDeposit(depositDto);
    }



}
