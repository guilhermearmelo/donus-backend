package com.donus.backend.exceptions;

public class CpfDoesntMatchPatternException extends Exception {

    @Override
    public String getMessage(){
        return "This CPF doesn't match the pattern 999.999.999-99";
    }
}