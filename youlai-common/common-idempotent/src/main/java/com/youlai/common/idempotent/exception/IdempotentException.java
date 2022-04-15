package com.youlai.common.idempotent.exception;


public class IdempotentException extends RuntimeException {

    public IdempotentException(String message) {
        super(message);
    }

}