package com.donus.backend.exceptions;

public class NotEnoughMoneyException extends Exception {

    @Override
    public String getMessage(){
        return "Not enough money!";
    }
}
