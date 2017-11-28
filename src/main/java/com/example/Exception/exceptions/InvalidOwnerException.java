package com.example.Exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Not the owner of the resource")
public class InvalidOwnerException extends RuntimeException{

}