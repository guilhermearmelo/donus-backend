package com.donus.backend.exceptions;

public class UserNotFoundException extends Exception {

    @Override
    public String getMessage(){
        return "This user id wasn't found in the database!";
    }
}
