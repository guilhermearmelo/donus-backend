package com.donus.backend.exceptions;

public class TokenDoesntMatchAccountCredentialsException extends Exception {

    @Override
    public String getMessage(){
        return "Token doesn't match account credentials!";
    }
}
