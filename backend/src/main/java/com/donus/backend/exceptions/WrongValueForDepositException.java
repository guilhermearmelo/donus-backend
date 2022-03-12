package com.donus.backend.exceptions;

public class WrongValueForDepositException extends Exception {

    @Override
    public String getMessage(){
        return "Deposit value must be between 0 and 2000";
    }
}