package com.donus.backend.service;

import com.donus.backend.config.security.TokenService;
import com.donus.backend.domain.Account;
import com.donus.backend.domain.Costumer;
import com.donus.backend.dto.*;
import com.donus.backend.exceptions.*;
import com.donus.backend.repository.AccountRepository;
import com.donus.backend.repository.CostumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CostumerRepository costumerRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    TokenService tokenService;

    public ResponseEntity<Object> insert(AccountInsertionDto accountInsertionDto, String token) {
        Account account = this.parseDtoToEntity(accountInsertionDto);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(!Objects.equals(tokenService.getIdCostumer(token.substring(7, token.length())), accountInsertionDto.getUserId()))
                throw new TokenDoesntMatchAccountCredentialsException();
            if(accountRepository.findByCostumer(accountInsertionDto.getUserId()) != null)
                throw new CostumerAlreadyHasAnAccountException();
            if(accountRepository.findByCode(accountInsertionDto.getAccountId()) != null)
                throw new AccountAlreadyExistsException();

            account.setKey(new BCryptPasswordEncoder().encode(accountInsertionDto.getPassword()));
            baseResponseDto.setData(parseEntityToDto(accountRepository.save(account)));
            baseResponseDto.setMessage("Account successfully created!");
            baseResponseDto.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CREATED);
        } catch (CostumerAlreadyHasAnAccountException e){
            baseResponseDto.setMessage(new CostumerAlreadyHasAnAccountException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        } catch (AccountAlreadyExistsException e){
            baseResponseDto.setMessage(new AccountAlreadyExistsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        } catch (TokenDoesntMatchAccountCredentialsException e){
            baseResponseDto.setMessage(new TokenDoesntMatchAccountCredentialsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Object> remove(long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(accountRepository.findById(id) == null)
                throw new AccountNotFoundException();

            Account account = accountRepository.findById(id);
            accountRepository.delete(account);

            baseResponseDto.setMessage("Account successfully deleted!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            baseResponseDto.setMessage(new AccountNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findAll(Pageable pageable) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Page<Account> accountList = accountRepository.findAll(pageable);
        baseResponseDto.setData(AccountDto.converter(accountList));

        if(accountList.isEmpty()){
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            baseResponseDto.setMessage("No account was found!");
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        }

        baseResponseDto.setStatusCode(HttpStatus.OK.value());
        baseResponseDto.setMessage("Accounts found!");
        return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> findById(long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(accountRepository.findById(id) == null)
                throw new AccountNotFoundException();

            Account account = accountRepository.findById(id);

            baseResponseDto.setData(parseEntityToDto(account));
            baseResponseDto.setMessage("Account found!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch (AccountNotFoundException e){
            baseResponseDto.setMessage(new AccountNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> doTransaction(TransactionDto transactionDto, String token) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(accountRepository.findByCode(transactionDto.getSourceAccount()) == null)
                throw new AccountNotFoundException();
            if(accountRepository.findByCode(transactionDto.getTargetAccount()) == null)
                throw new AccountNotFoundException();
            Account account = accountRepository.findByCode(transactionDto.getSourceAccount());
            if(!Objects.equals(tokenService.getIdCostumer(token.substring(7, token.length())), account.getCostumer().getId()))
                throw new TokenDoesntMatchAccountCredentialsException();
            if(!new BCryptPasswordEncoder().matches(transactionDto.getUserKey(), account.getKey()))
                throw new AccountKeyDoesntMatchDatabaseException();

            Account source = accountRepository.findByCode(transactionDto.getSourceAccount());
            Account target = accountRepository.findByCode(transactionDto.getTargetAccount());
            Double amount = transactionDto.getAmount();

            Pattern p = Pattern.compile("^[1-9]\\d*(\\.\\d+)?$");
            Matcher m = p.matcher(amount.toString());

            if(!source.hasMoney(amount))
                throw new NotEnoughMoneyException();
            if(!m.matches())
                throw new AmountDoesntMatchPatternException();

            source.setBalance(source.getBalance() - amount);
            target.setBalance(target.getBalance() + amount);

            accountRepository.save(source);
            accountRepository.save(target);

            baseResponseDto.setData(new AccountDto(source));
            baseResponseDto.setMessage("Transaction completed successfully!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());

            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch(AccountNotFoundException e){
            baseResponseDto.setMessage(new AccountNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.NOT_FOUND);
        } catch(NotEnoughMoneyException e){
            baseResponseDto.setMessage(new NotEnoughMoneyException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch(AmountDoesntMatchPatternException e){
            baseResponseDto.setMessage(new AmountDoesntMatchPatternException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch (TokenDoesntMatchAccountCredentialsException e){
            baseResponseDto.setMessage(new TokenDoesntMatchAccountCredentialsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.UNAUTHORIZED);
        } catch (AccountKeyDoesntMatchDatabaseException e){
            baseResponseDto.setMessage(new AccountKeyDoesntMatchDatabaseException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<Object> makeDeposit(DepositDto depositDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        Double amount = depositDto.getAmount();

        try{

            if(accountRepository.findByCode(depositDto.getTargetAccount()) == null)
                throw new AccountNotFoundException();
            if(!((0<amount)&&(amount<=2000)))
                throw new WrongValueForDepositException();

            Account target = accountRepository.findByCode(depositDto.getTargetAccount());
            target.setBalance(target.getBalance() + amount);
            accountRepository.save(target);
            baseResponseDto.setMessage("Deposit successfully done!");
            baseResponseDto.setStatusCode(HttpStatus.OK.value());

            return new ResponseEntity<>(baseResponseDto, HttpStatus.OK);
        } catch(AccountNotFoundException e){
            baseResponseDto.setMessage(new AccountNotFoundException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        } catch(WrongValueForDepositException e){
            baseResponseDto.setMessage(new WrongValueForDepositException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.BAD_REQUEST);
        }

    }

    public Account parseDtoToEntity(AccountDto accountDto) {

        return new Account(accountDto.getCode(), accountDto.getBalance());
    }

    public Account parseDtoToEntity(AccountInsertionDto accountInsertionDto) {
        Account account = new Account(accountInsertionDto.getAccountId(), accountInsertionDto.getBalance());
        Costumer costumer = costumerRepository.findById((long)accountInsertionDto.getUserId());
        account.setCostumer(costumer);

        return account;
    }

    public AccountDto parseEntityToDto(Account account){

        return new AccountDto(account);
    }

}
