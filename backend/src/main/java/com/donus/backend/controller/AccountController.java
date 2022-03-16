package com.donus.backend.controller;

import com.donus.backend.dto.*;
import com.donus.backend.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
@CrossOrigin(origins="*")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/account")
    @ApiOperation(value = "Creates an account for a given user Id")
    public ResponseEntity<Object> insertAccount(@RequestBody AccountInsertionDto accountInsertionDto, @RequestHeader("Authorization") String token) {
        return accountService.insert(accountInsertionDto, token);
    }

    @DeleteMapping("/account/{id}")
    @ApiOperation(value = "Removes an account given an id")
    public ResponseEntity<Object> deleteAccountById(@PathVariable(value="id") long id) {
        return accountService.remove(id);
    }

    @GetMapping("/accounts")
    @ApiOperation(value = "Returns a list of all accounts")
    public ResponseEntity<Object> listUsers(@PageableDefault(sort="id", direction=Sort.Direction.DESC, page=0, size=10) Pageable pageable){
        return accountService.findAll(pageable);
    }

    @GetMapping("/account/{id}")
    @ApiOperation(value = "Returns one account given an id")
    public ResponseEntity<Object> findAccountById(@PathVariable(value="id") long id) {
        return accountService.findById(id);
    }

    @PutMapping("/transaction")
    @ApiOperation(value = "Makes balance transaction between two accounts")
    public ResponseEntity<Object> transactionBetweenAccounts(@RequestBody TransactionDto transactionDto, @RequestHeader("Authorization") String token) {
        return accountService.doTransaction(transactionDto, token);
    }

    @PutMapping("/deposit")
    @ApiOperation(value = "Deposit a certain amount in a given account")
    public ResponseEntity<Object> depositInAccount(@RequestBody DepositDto depositDto) {
        return accountService.makeDeposit(depositDto);
    }

}
