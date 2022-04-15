package com.youlai.common.redis.utils;

import com.youlai.common.redis.distributeLock.redission.IRedissionLocker;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/22 20:04
 * @description: redission分布式锁工具类
 */
@Component
public class RedissionLockUtils {

    @Autowired
    private   IRedissionLocker redissLock;

    public   void setLocker(IRedissionLocker locker) {
        redissLock = locker;
    }

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public   RLock lock(String lockKey) {
        return redissLock.lock(lockKey);
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public   void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    /**
     * 释放锁
     * @param lock
     */
    public   void unlock(RLock lock) {
        redissLock.unlock(lock);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public   RLock lock(String lockKey, int timeout) {
        return redissLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public   RLock lock(String lockKey, TimeUnit unit , int timeout) {
        return redissLock.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public   boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public   boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, unit, waitTime, leaseTime);
    }

    /**
     * 递增
     * @param key
     * @param delta 要增加几(大于0)
     * @return int
     */
    public   int incr(String key, int delta) {
        return redissLock.incr(key,delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return int
     */
    public   int decr(String key, int delta) {
        return redissLock.decr(key,delta);
    }


    /**
     * @description 获取信号量
     * @param key
     * @return org.redisson.api.RSemaphore
     * @author xiezhiyan
     * @date 2022/2/25 10:30:30
     */
    public   RSemaphore getSemaphore(String key) {
        return redissLock.getSemaphore(key);
    }

}




