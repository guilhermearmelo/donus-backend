package com.donus.backend.exceptions;

public class CostumerNotFoundException extends Exception {

    @Override
    public String getMessage(){
        return "This costumer id wasn't found in the database!";
    }
}
