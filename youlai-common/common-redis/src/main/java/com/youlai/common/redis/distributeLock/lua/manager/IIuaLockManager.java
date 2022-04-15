package com.youlai.common.redis.distributeLock.lua.manager;

/**
 * @author: xiezhiyan
 * @createTime: 2022/02/23 20:58
 * @description: lua锁管理器
 */
public interface IIuaLockManager {
    /**
     * 加锁并执行业务
     * @param lockKey
     * @param callBack
     */
    void callBack(String lockKey, ILockCallBack callBack);

    /**
     * 加锁并返回执行业务的返回值
     * @param lockKey
     * @param callBack
     * @param <T>
     * @return
     */
    <T> T callBack(String lockKey, IReturnCallBack<T> callBack);
}

