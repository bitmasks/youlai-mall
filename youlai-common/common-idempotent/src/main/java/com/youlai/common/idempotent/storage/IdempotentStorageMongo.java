package com.youlai.common.idempotent.storage;

import com.youlai.common.idempotent.enums.IdempotentStorageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class IdempotentStorageMongo implements IdempotentStorage {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public IdempotentStorageTypeEnum type() {
        return IdempotentStorageTypeEnum.MONGO;
    }

    @Override
    public void setValue(String key, String value, long expireTime, TimeUnit timeUnit) {
        log.debug("Mongo Set key:{}, Value:{}, expireTime:{}, timeUnit:{}", key, value, expireTime, timeUnit);
        Date date = new Date();

        IdempotentRecord record = new IdempotentRecord();
        record.setKey(key);
        record.setValue(value);
        record.setAddTime(date);

        long millis = timeUnit.toMillis(expireTime);
        record.setExpireTime(new Date(date.getTime() + millis));
        mongoTemplate.save(record, COLL_NAME);
    }

    @Override
    public String getValue(String key) {
        IdempotentRecord record = mongoTemplate.findOne(Query.query(Criteria.where("key").is(key)), IdempotentRecord.class, COLL_NAME);
        String value = record == null ? null : record.getValue();
        log.debug("Mongo Get key:{}, Value:{}", key, value);
        return value;
    }

}
