package com.youlai.common.idempotent.storage;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class IdempotentRecord {

    @Id
    private String id;

    private String key;

    private String value;

    private Date addTime;

    private Date expireTime;

}
