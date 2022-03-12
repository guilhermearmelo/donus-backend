package com.donus.backend.exceptions;

public class AccountNotFoundException extends Exception {

    @Override
    public String getMessage(){
        return "This account id wasn't found in the database!";
    }
}
