package com.youlai.common.idempotent.storage;


import com.youlai.common.idempotent.enums.IdempotentStorageTypeEnum;

import java.util.concurrent.TimeUnit;


public interface IdempotentStorage {

    String COLL_NAME = "idempotent_record";

    IdempotentStorageTypeEnum type();

    void setValue(String key, String value, long expireTime, TimeUnit timeUnit);

    String getValue(String key);

}
