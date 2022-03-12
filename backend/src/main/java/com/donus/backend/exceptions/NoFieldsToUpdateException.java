package com.donus.backend.exceptions;

public class NoFieldsToUpdateException extends Exception {

    @Override
    public String getMessage(){
        return "No fields to update!";
    }
}
