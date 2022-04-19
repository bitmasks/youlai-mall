package com.youlai.common.mybatis.config;

import com.youlai.common.mybatis.lock.IDistributedLockMysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author xiezhiyan
 * 2022/04/18 19:55
 */
@Component
public class DistributedLockMysqlUtils {
    @Autowired
    private IDistributedLockMysql distributedLockMysql;

    public <T> T lock(String key, int waitTime, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return distributedLockMysql.lock(key, waitTime, leaseTime, success, fail);
    }

    public <T> T lock(String key, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return distributedLockMysql.lock(key, 0, leaseTime, success, fail);
    }


    public <T> T lock(String key, int leaseTime, TimeUnit timeUnit, Supplier<T> success, Supplier<T> fail) {
        // todo: Mysql锁自动失效待实现
        return distributedLockMysql.lock(key, 0, leaseTime, success, fail);
    }


    public void lock(String key, int waitTime, int leaseTime, Runnable success, Runnable fail) {
        distributedLockMysql.lock(key, waitTime, leaseTime, success, fail);
    }


    public void lock(String key, int leaseTime, Runnable success, Runnable fail) {
        distributedLockMysql.lock(key, 0, leaseTime, success, fail);
    }


    public void lock(String key, int leaseTime, TimeUnit timeUnit, Runnable success, Runnable fail) {
        distributedLockMysql.lock(key, 0, leaseTime, success, fail);
    }
}
