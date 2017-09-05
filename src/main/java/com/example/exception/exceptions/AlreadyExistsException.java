package com.example.exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.SEE_OTHER, reason = "Already Exists")
public class AlreadyExistsException extends RuntimeException{

}