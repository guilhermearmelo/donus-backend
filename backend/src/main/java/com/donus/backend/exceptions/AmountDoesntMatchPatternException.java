package com.donus.backend.exceptions;

public class AmountDoesntMatchPatternException extends Exception {

    @Override
    public String getMessage(){
        return "The amount must be a number greater than zero!";
    }
}