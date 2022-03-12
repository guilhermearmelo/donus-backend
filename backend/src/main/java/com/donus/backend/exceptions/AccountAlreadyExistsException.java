package com.donus.backend.exceptions;

public class AccountAlreadyExistsException extends Exception {

    @Override
    public String getMessage(){
        return "This account code already exists in the database!";
    }
}