package com.example.rechick.exception.custom;


import com.example.rechick.common.BaseResponseStatus;

public class InvalidUserException extends InvalidCustomException{
    public InvalidUserException(BaseResponseStatus status){
        super(status);
    }
}