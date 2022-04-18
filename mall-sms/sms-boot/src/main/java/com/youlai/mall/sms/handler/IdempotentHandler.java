package com.youlai.mall.sms.handler;


import com.youlai.common.idempotent.exception.IdempotentException;

public class IdempotentHandler {

    public static void idempotentHandler(IdempotentException e) {
        System.out.println("idempotentHandler进来了。。。。");
    }

}
