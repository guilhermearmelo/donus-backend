package com.donus.backend.exceptions;

public class CpfAlreadyExistsException extends Exception {

    @Override
    public String getMessage(){
        return "This CPF already exists in the database!";
    }
}