package com.donus.backend.exceptions;

public class CostumerAlreadyHasAnAccountException extends Exception {

    @Override
    public String getMessage(){
        return "This costumer already has an account!";
    }
}
