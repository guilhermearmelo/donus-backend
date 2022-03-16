package com.donus.backend.exceptions;

public class AccountKeyDoesntMatchDatabaseException extends Exception{

    @Override
    public String getMessage(){
        return "Account key doesn't match database!";
    }

}
