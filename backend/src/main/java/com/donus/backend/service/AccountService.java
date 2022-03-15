package com.donus.backend.service;

import com.donus.backend.domain.Account;
import com.donus.backend.domain.User;
import com.donus.backend.dto.*;
import com.donus.backend.exceptions.*;
import com.donus.backend.repository.AccountRepository;
import com.donus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    public ResponseEntity<Object> insert(AccountInsertionDto accountInsertionDto) {
        Account account = this.parseDtoToEntity(accountInsertionDto);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(!authService.validateUserPassword(accountInsertionDto.getPassword(), accountInsertionDto.getUserId()))
                throw new AuthenticationException();
            if(accountRepository.findByUser(accountInsertionDto.getUserId()) != null)
                throw new UserAlreadyHasAnAccountException();
            if(accountRepository.findByCode(accountInsertionDto.getAccountId()) != null)
                throw new AccountAlreadyExistsException();

            baseResponseDto.setData(parseEntityToDto(accountRepository.save(account)));
            baseResponseDto.setMessage("Account successfully created!");
            baseResponseDto.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CREATED);
        } catch (UserAlreadyHasAnAccountException e){
            baseResponseDto.setMessage(new UserAlreadyHasAnAccountException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        } catch (AccountAlreadyExistsException e){
            baseResponseDto.setMessage(new AccountAlreadyExistsException().getMessage());
            baseResponseDto.setStatusCode(HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(baseResponseDto, HttpStatus.CONFLICT);
        } catch (AuthenticationException e){
            baseResponseDto.setMessage(new AuthenticationException().getMessage());
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

    public ResponseEntity<Object> doTransaction(TransactionDto transactionDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        try{
            if(accountRepository.findByCode(transactionDto.getSourceAccount()) == null)
                throw new AccountNotFoundException();
            if(accountRepository.findByCode(transactionDto.getTargetAccount()) == null)
                throw new AccountNotFoundException();
            if(!authService.validateUserAccount(transactionDto.getSourceAccount(), transactionDto.getUserKey()))
                throw new AuthenticationException();

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
        } catch(AuthenticationException e){
            baseResponseDto.setMessage(new AuthenticationException().getMessage());
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

            Account target = accountRepository.findByCode(depositDto.getTargetAccount());

            if(!((0<amount)&&(amount<=2000)))
                throw new WrongValueForDepositException();


            target.setBalance(target.getBalance() + amount);
            accountRepository.save(target);
            baseResponseDto.setData(new AccountDto(target));
            baseResponseDto.setMessage("Deposit successfully done!");
            baseResponseDto.setStatusCode(HttpStatus.CREATED.value());

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
