package com.youlai.common.redis.distributeLock.redission;

import com.youlai.common.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.redisson.client.WriteRedisConnectionException;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 15:08
 * @description: redission分布式锁
 */
@Slf4j
@Component
public class RedissonDistributedLocker implements IRedissionLocker , DistributedLock {
    private RedissonClient redissonClient;

    public RedissonDistributedLocker(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit , int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    @Override
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public int incr(String key, int delta) {
        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("skill");
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        /*加1并获取计算后的值*/
        return  mapCache.addAndGet(key, 1);
    }

    @Override
    public int decr(String key, int delta) {
        RMapCache<String, Integer> mapCache = redissonClient.getMapCache("skill");
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        /*加1并获取计算后的值*/
        return mapCache.addAndGet(key, -delta);
    }

    /**************************************带成功失败状态的锁********************************************************/
    @Override
    public RSemaphore getSemaphore(String key) {
        return redissonClient.getSemaphore(key);
    }
    @Override
    public <T> T lock(String key, int waitTime, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return doLock(key, waitTime, leaseTime, TimeUnit.MILLISECONDS, success, fail);
    }

    @Override
    public <T> T lock(String key, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return doLock(key, 0, leaseTime, TimeUnit.MILLISECONDS, success, fail);
    }

    @Override
    public <T> T lock(String key, int leaseTime, TimeUnit timeUnit, Supplier<T> success, Supplier<T> fail) {
        return doLock(key, 0, leaseTime, timeUnit, success, fail);
    }

    @Override
    public void lock(String key, int waitTime, int leaseTime, Runnable success, Runnable fail) {
        doLock(key, waitTime, leaseTime, TimeUnit.MILLISECONDS, success, fail);
    }

    @Override
    public void lock(String key, int leaseTime, Runnable success, Runnable fail) {
        doLock(key, 0, leaseTime, TimeUnit.MILLISECONDS, success, fail);
    }

    @Override
    public void lock(String key, int leaseTime, TimeUnit timeUnit, Runnable success, Runnable fail) {
        doLock(key, 0, leaseTime, timeUnit, success, fail);
    }

    private <T> T doLock(String key, int waitTime, int leaseTime, TimeUnit timeUnit, Supplier<T> success, Supplier<T> fail) {
        try {
            RLock lock = null;
            try {
                lock = redissonClient.getLock(key);
            } catch (Exception e) {
                log.error("get Redis Lock Error", e);
                return fail.get();
            }

            boolean tryLock = false;
            try {
                tryLock = lock.tryLock(waitTime, leaseTime, timeUnit);
            } catch (WriteRedisConnectionException e) {
                log.error(" Redis tryLock Error", e);
                return fail.get();
            }

            if (!tryLock) {
                return fail.get();
            }

            try {
                return success.get();
            } catch (Exception e){
                throw e;
            } finally {
                if (lock.getHoldCount() != 0) {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void doLock(String key, int waitTime, int leaseTime, TimeUnit timeUnit, Runnable success, Runnable fail) {
        try {
            RLock lock = null;
            try {
                lock = redissonClient.getLock(key);
            } catch (Exception e) {
                log.error("get Redis Lock Error", e);
                return;
            }

            boolean tryLock = false;
            try {
                tryLock = lock.tryLock(waitTime, leaseTime, timeUnit);
            } catch (WriteRedisConnectionException e) {
                log.error(" Redis tryLock Error", e);
                return;
            }

            if (!tryLock) {
                fail.run();
                return;
            }

            try {
                success.run();
            } catch (Exception e){
                throw e;
            } finally {
                if (lock.getHoldCount() != 0) {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
