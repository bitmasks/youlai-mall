package com.youlai.common.redis.distributeLock.redission;

import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 15:05
 * @description: 分布式锁接口
 */
public interface IRedissionLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    void setRedissonClient(RedissonClient redissonClient);

    int incr(String key, int delta);

    int decr(String key, int delta);

    RSemaphore getSemaphore(String key);
}
