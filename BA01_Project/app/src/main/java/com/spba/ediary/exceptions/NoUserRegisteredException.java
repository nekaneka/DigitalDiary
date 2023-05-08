package com.spba.ediary.exceptions;

public class NoUserRegisteredException extends Exception{

    public NoUserRegisteredException(String errorMessage){
        super(errorMessage);
    }
}
