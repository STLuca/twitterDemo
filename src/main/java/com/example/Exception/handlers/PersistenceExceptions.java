package com.example.Exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.NoResultException;


@ControllerAdvice
@RestController
public class PersistenceExceptions {

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String noResultException(NoResultException e){
        return "No results found";
    }

}
