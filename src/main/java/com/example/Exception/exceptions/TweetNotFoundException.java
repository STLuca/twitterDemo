package com.example.Exception.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TweetNotFoundException extends RuntimeException{

    public TweetNotFoundException(Long id){
        super("Could not find Tweet '" + id + "'.");
    }


}
