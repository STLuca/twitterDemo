package com.example.exception.handlers;

import com.example.exception.exceptions.InvalidOwnerException;
import com.example.exception.exceptions.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;


@ControllerAdvice
@RestController
public class PersistenceExceptions {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String EntityNotFound(EntityNotFoundException e){
        return "Resource Not Found";
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String EntityExists(EntityExistsException e){
        return "Entity already Exists";
    }

    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String noResultException(NoResultException e){
        return "No results found";
    }

}
