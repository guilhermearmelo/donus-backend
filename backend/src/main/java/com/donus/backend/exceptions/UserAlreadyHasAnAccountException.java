package com.donus.backend.exceptions;

public class UserAlreadyHasAnAccountException extends Exception {

    @Override
    public String getMessage(){
        return "This user already has an account!";
    }
}
