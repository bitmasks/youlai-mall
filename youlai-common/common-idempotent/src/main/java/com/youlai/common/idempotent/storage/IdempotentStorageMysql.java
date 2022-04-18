package com.youlai.common.idempotent.storage;


import com.youlai.common.idempotent.enums.IdempotentStorageTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class IdempotentStorageMysql implements IdempotentStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public IdempotentStorageTypeEnum type() {
        return IdempotentStorageTypeEnum.MYSQL;
    }

    @Override
    public void setValue(String key, String value, long expireTime, TimeUnit timeUnit) {
        log.debug("Mysql Set key:{}, Value:{}, expireTime:{}, timeUnit:{}", key, value, expireTime, timeUnit);
        Date date = new Date();
        long millis = timeUnit.toMillis(expireTime);
        Date expireDate = new Date(date.getTime() + millis);
        String sql = "insert into idempotent_record(`key`,`value`,addTime,expireTime) values(?,?,?,?)";
        jdbcTemplate.update(sql, key, value, date, expireDate);
    }

    @Override
    public String getValue(String key) {
        String sql = "select `value` from idempotent_record where `key` = ? limit 1";
        List<IdempotentRecord> records = jdbcTemplate.query(sql,  new BeanPropertyRowMapper(IdempotentRecord.class), key);
        String value = null;
        if (!CollectionUtils.isEmpty(records)) {
            value = records.get(0).getValue();
        }
        log.debug("Mysql Get key:{}, Value:{}", key, value);
        return value;
    }
}
