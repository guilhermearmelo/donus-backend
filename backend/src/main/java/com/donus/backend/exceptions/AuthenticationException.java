package com.donus.backend.exceptions;

public class AuthenticationException extends Exception {

    @Override
    public String getMessage(){
        return "Authentication error!";
    }
}