package com.example.Exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String email){
        super("Email '" + email + "' already exists.");
    }

}